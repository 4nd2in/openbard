package ch.openbard.app.redux.reducers

import ch.openbard.app.redux.AppState
import ch.smoca.redux.Action
import ch.smoca.redux.Reducer

class PlayerReducer : Reducer<AppState> {
    sealed class PlayerStateAction : Action {
        data class UpdateCurrentSong(
            val songId: Long?,
        ) : PlayerStateAction()

        data class UpdateIsInitialized(
            val isInitialized: Boolean,
        ) : PlayerStateAction()

        data class UpdateIsPlaying(
            val isPlaying: Boolean,
        ) : PlayerStateAction()

        data class UpdateShuffleMode(
            val enabled: Boolean,
        ) : PlayerStateAction()

        data class UpdateRepeatMode(
            val enabled: Boolean,
        ) : PlayerStateAction()

        data class UpdateCurrentSongProgress(
            val progress: Long,
        ) : PlayerStateAction()

        data class UpdateCurrentPlaylist(
            val playlist: List<Long>,
        ) : PlayerStateAction()
    }

    override fun reduce(
        action: Action,
        state: AppState,
    ): AppState {
        if (action !is PlayerStateAction) return state

        return when (action) {
            is PlayerStateAction.UpdateCurrentSong -> {
                state.copy(player = state.player.copy(currentlyPlayingSongId = action.songId))
            }

            is PlayerStateAction.UpdateIsInitialized -> {
                state.copy(player = state.player.copy(isInitialized = action.isInitialized))
            }

            is PlayerStateAction.UpdateIsPlaying -> {
                state.copy(player = state.player.copy(isPlaying = action.isPlaying))
            }

            is PlayerStateAction.UpdateShuffleMode -> {
                state.copy(player = state.player.copy(isShuffleOn = action.enabled))
            }

            is PlayerStateAction.UpdateRepeatMode -> {
                state.copy(player = state.player.copy(isRepeatOn = action.enabled))
            }

            is PlayerStateAction.UpdateCurrentSongProgress -> {
                state.copy(
                    player = state.player.copy(currentlyPlayingSongProgress = action.progress),
                )
            }

            is PlayerStateAction.UpdateCurrentPlaylist -> {
                state.copy(player = state.player.copy(currentPlaylist = action.playlist))
            }
        }
    }
}
