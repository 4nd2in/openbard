package ch.openbard.app.redux.sagas

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.PlaybackParameters
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import ch.openbard.app.redux.AppState
import ch.openbard.app.redux.reducers.PlayerReducer
import ch.smoca.redux.Action
import ch.smoca.redux.sagas.Saga

class PlayerSaga(context: Context) : Saga<AppState>(), Player.Listener {
    sealed class PlayerAction : Action {
        data object Play : PlayerAction()
        data object Pause : PlayerAction()
        data class Seek(val whereTo: Long) : PlayerAction()
        data object Stop : PlayerAction()
        data object Release : PlayerAction()
        data object UpdatePosition : PlayerAction()
        data class SetDataSource(val sourceUrl: String) : PlayerAction()
    }

    private val mainHandler = Handler(Looper.getMainLooper())
    private val player = ExoPlayer.Builder(context).build()

    init {
        mainHandler.post {
            player.setWakeMode(C.WAKE_MODE_LOCAL)
        }
    }

    override suspend fun onAction(
        action: Action,
        oldState: AppState,
        newState: AppState,
    ) {
        if (action !is PlayerAction) return

        when (action) {
            PlayerAction.Play -> play()
            PlayerAction.Pause -> pause()
            is PlayerAction.Seek -> seek(action.whereTo)
            PlayerAction.Stop -> stop()
            PlayerAction.Release -> release()
            PlayerAction.UpdatePosition -> position(newState.player.isInitialized)
            is PlayerAction.SetDataSource -> setDataSource(action.sourceUrl)
        }
    }

    @Suppress("TooGenericExceptionCaught")
    private fun setDataSource(sourceUrl: String) {
        dispatch(PlayerReducer.PlayerAction.UpdateIsInitialized(false))
        val mediaItem = MediaItem.fromUri(sourceUrl)
        try {
            mainHandler.post {
                player.setMediaItem(mediaItem)
                player.setAudioAttributes(
                    AudioAttributes
                        .Builder()
                        .setUsage(C.USAGE_MEDIA)
                        .setContentType(C.AUDIO_CONTENT_TYPE_MUSIC)
                        .build(),
                    false,
                )
                player.playbackParameters = PlaybackParameters(1.0f, 1.0f)

                player.addListener(
                    object : Player.Listener {
                        override fun onPlaybackStateChanged(state: Int) {
                            if (state == Player.STATE_READY) {
                                player.removeListener(this)
                                dispatch(PlayerReducer.PlayerAction.UpdateIsInitialized(true))
                            }
                        }
                    },
                )
                player.addListener(this)
                player.prepare()
            }
        } catch (e: Exception) {
            Log.e("${javaClass.simpleName}", "${e.message}")
        }
    }

    fun play() {
        try {
            mainHandler.post {
                player.play()
            }
            dispatch(PlayerReducer.PlayerAction.UpdateIsPlaying(true))
        } catch (e: IllegalStateException) {
            Log.e("${javaClass.simpleName}", "${e.message}")
            dispatch(PlayerReducer.PlayerAction.UpdateIsPlaying(false))
        }
    }

    fun pause() {
        try {
            mainHandler.post {
                player.pause()
            }
            dispatch(PlayerReducer.PlayerAction.UpdateIsPlaying(false))
        } catch (e: IllegalStateException) {
            Log.e("${javaClass.simpleName}", "${e.message}")
            dispatch(PlayerReducer.PlayerAction.UpdateIsPlaying(true))
        }
    }

    fun position(isInitialized: Boolean) {
        if (!isInitialized) {
            dispatch(PlayerReducer.PlayerAction.UpdatePosition(-1))
        } else {
            try {
                mainHandler.post {
                    dispatch(PlayerReducer.PlayerAction.UpdatePosition(player.currentPosition))
                }
            } catch (e: IllegalStateException) {
                Log.e("${javaClass.simpleName}", "${e.message}")
                dispatch(PlayerReducer.PlayerAction.UpdatePosition(-1))
            }
        }
    }

    fun seek(whereto: Long) {
        try {
            mainHandler.post {
                player.seekTo(whereto)
            }
            true
        } catch (e: IllegalStateException) {
            Log.e("${javaClass.simpleName}", "${e.message}")
            false
        }
    }

    fun stop() {
        mainHandler.post {
            player.stop()
        }
        dispatch(PlayerReducer.PlayerAction.UpdateIsInitialized(false))
    }

    fun release() {
        stop()
        mainHandler.post {
            player.release()
        }
    }

    override fun onPlayerError(error: PlaybackException) {
        Log.e("${javaClass.simpleName}", "${error.message}")
        dispatch(PlayerReducer.PlayerAction.UpdateIsInitialized(false))
        mainHandler.post {
            player.release()
        }
    }
}
