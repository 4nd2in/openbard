package ch.openbard.app.redux.reducers

import ch.openbard.app.redux.AppState
import ch.openbard.app.redux.Song
import ch.smoca.redux.Action
import ch.smoca.redux.Reducer

class SongsReducer : Reducer<AppState> {
    sealed class SongsAction : Action {
        data class UpdateSongs(
            val songs: List<Song>,
        ) : SongsAction()

        data class UpdateLastScan(
            val lastScannedAt: Long,
        ) : SongsAction()
    }

    override fun reduce(
        action: Action,
        state: AppState,
    ): AppState {
        if (action !is SongsAction) return state

        return when (action) {
            is SongsAction.UpdateLastScan -> state.copy(lastScannedAt = action.lastScannedAt)
            is SongsAction.UpdateSongs -> state.copy(songs = action.songs)
        }
    }
}
