package ch.openbard.app.redux.sagas

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.PlaybackParameters
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import ch.openbard.app.player.PlaybackService
import ch.openbard.app.redux.AppState
import ch.openbard.app.redux.Song
import ch.openbard.app.redux.reducers.PlayerReducer
import ch.openbard.app.redux.toMediaItem
import ch.smoca.redux.Action
import ch.smoca.redux.sagas.Saga
import com.google.common.util.concurrent.MoreExecutors
import java.lang.Exception

@Suppress("TooManyFunctions")
class PlayerSaga(
    context: Context,
) : Saga<AppState>(),
    Player.Listener {
    sealed class PlayerAction : Action {
        data object Play : PlayerAction()

        data object Pause : PlayerAction()

        data class SeekTo(
            val whereTo: Long,
        ) : PlayerAction()

        data object SeekToNext : PlayerAction()

        data object SeekToPrevious : PlayerAction()

        data class SetShuffleMode(
            val enabled: Boolean,
        ) : PlayerAction()

        data class SetRepeatMode(
            val enabled: Boolean,
        ) : PlayerAction()

        data object Stop : PlayerAction()

        data object Release : PlayerAction()

        data object UpdatePosition : PlayerAction()
    }

    private val mainHandler = Handler(Looper.getMainLooper())
    private lateinit var controller: MediaController

    init {
        val playbackService = Intent(context, PlaybackService::class.java)
        val playbackComponent = ComponentName(context, PlaybackService::class.java)
        context.startService(playbackService)

        val sessionToken = SessionToken(context, playbackComponent)
        val controllerFuture = MediaController.Builder(context, sessionToken).buildAsync()
        controllerFuture.addListener(
            { controller = controllerFuture.get() },
            MoreExecutors.directExecutor(),
        )
    }

    override suspend fun onAction(
        action: Action,
        oldState: AppState,
        newState: AppState,
    ) {
        when (action) {
            PlayerAction.Play -> executeSafely { controller.play() }
            PlayerAction.Pause -> executeSafely { controller.pause() }
            is PlayerAction.SeekTo -> executeSafely { controller.seekTo(action.whereTo) }
            PlayerAction.SeekToNext -> executeSafely { controller.seekToNextMediaItem() }
            PlayerAction.SeekToPrevious -> executeSafely { controller.seekToPreviousMediaItem() }
            is PlayerAction.SetShuffleMode -> {
                executeSafely { controller.shuffleModeEnabled = action.enabled }
            }

            is PlayerAction.SetRepeatMode ->
                executeSafely {
                    controller.repeatMode =
                        if (action.enabled) Player.REPEAT_MODE_ALL else Player.REPEAT_MODE_OFF
                }

            PlayerAction.Stop -> stop()
            PlayerAction.Release -> release()
            PlayerAction.UpdatePosition -> position(newState.player.isInitialized)
            is PlayerReducer.PlayerStateAction.UpdateCurrentPlaylist -> {
                setMediaItems(
                    songToStart = newState.player.currentlyPlayingSongId,
                    songs = newState.songs.filterKeys { it in action.playlist },
                )
            }
        }
    }

    private fun setMediaItems(
        songToStart: Long?,
        songs: Map<Long, Song>,
    ) {
        dispatch(PlayerReducer.PlayerStateAction.UpdateIsInitialized(false))
        val entries = songs.entries.toList()
        val mediaItems = songs.map { it.toMediaItem() }
        val startIndex = entries.indexOfFirst { it.key == songToStart }
        executeSafely {
            controller.setMediaItems(mediaItems, startIndex, 0)
            controller.setAudioAttributes(
                AudioAttributes
                    .Builder()
                    .setUsage(C.USAGE_MEDIA)
                    .setContentType(C.AUDIO_CONTENT_TYPE_MUSIC)
                    .build(),
                false,
            )
            controller.playbackParameters = PlaybackParameters(1.0f, 1.0f)

            controller.addListener(
                object : Player.Listener {
                    override fun onPlaybackStateChanged(state: Int) {
                        if (state == Player.STATE_READY) {
                            controller.removeListener(this)
                            dispatch(PlayerReducer.PlayerStateAction.UpdateIsInitialized(true))
                            executeSafely { controller.play() }
                        }
                    }
                },
            )
            controller.addListener(this)
            controller.prepare()
        }
    }

    @Suppress("TooGenericExceptionCaught")
    private fun executeSafely(block: () -> Unit) {
        try {
            mainHandler.post {
                block()
            }
        } catch (e: Exception) {
            Log.e("${javaClass.simpleName}", "${e.message}")
        }
    }

    private fun position(isInitialized: Boolean) {
        if (!isInitialized) {
            dispatch(PlayerReducer.PlayerStateAction.UpdateCurrentSongProgress(-1))
        } else {
            try {
                mainHandler.post {
                    dispatch(
                        PlayerReducer.PlayerStateAction.UpdateCurrentSongProgress(
                            controller.currentPosition,
                        ),
                    )
                }
            } catch (e: IllegalStateException) {
                Log.e("${javaClass.simpleName}", "${e.message}")
                dispatch(
                    PlayerReducer.PlayerStateAction.UpdateCurrentSongProgress(-1),
                )
            }
        }
    }

    private fun stop() {
        mainHandler.post {
            controller.stop()
        }
        dispatch(PlayerReducer.PlayerStateAction.UpdateIsInitialized(false))
    }

    private fun release() {
        stop()
        mainHandler.post {
            controller.removeListener(this)
            controller.release()
        }
    }

    override fun onIsPlayingChanged(isPlaying: Boolean) {
        dispatch(PlayerReducer.PlayerStateAction.UpdateIsPlaying(isPlaying))
    }

    override fun onShuffleModeEnabledChanged(shuffleModeEnabled: Boolean) {
        super.onShuffleModeEnabledChanged(shuffleModeEnabled)
        dispatch(PlayerReducer.PlayerStateAction.UpdateShuffleMode(shuffleModeEnabled))
    }

    override fun onRepeatModeChanged(repeatMode: Int) {
        super.onRepeatModeChanged(repeatMode)
        val isRepeatOn = repeatMode in listOf(Player.REPEAT_MODE_ALL, Player.REPEAT_MODE_ONE)
        dispatch(PlayerReducer.PlayerStateAction.UpdateRepeatMode(isRepeatOn))
    }

    override fun onMediaItemTransition(
        mediaItem: MediaItem?,
        reason: Int,
    ) {
        super.onMediaItemTransition(mediaItem, reason)
        mediaItem?.mediaId?.toLongOrNull()?.let { songId ->
            dispatch(PlayerReducer.PlayerStateAction.UpdateCurrentSong(songId))
        }
    }

    override fun onPlayerError(error: PlaybackException) {
        Log.e("${javaClass.simpleName}", "${error.message}")
        dispatch(PlayerReducer.PlayerStateAction.UpdateIsInitialized(false))
        mainHandler.post {
            controller.release()
        }
    }
}
