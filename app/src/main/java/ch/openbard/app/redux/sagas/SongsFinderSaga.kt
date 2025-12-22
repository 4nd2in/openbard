package ch.openbard.app.redux.sagas

import android.content.Context
import ch.openbard.app.player.MediaStoreQuery
import ch.openbard.app.redux.AppState
import ch.openbard.app.redux.reducers.SongsReducer
import ch.smoca.redux.Action
import ch.smoca.redux.sagas.Saga

class SongsFinderSaga(private val context: Context) : Saga<AppState>() {
    sealed class SongsFinderAction : Action {
        object Query : SongsFinderAction()
    }

    override suspend fun onAction(
        action: Action,
        oldState: AppState,
        newState: AppState,
    ) {
        if (action !is SongsFinderAction) return

        when (action) {
            SongsFinderAction.Query -> {
                val mediaStoreQuery = MediaStoreQuery(newState.lastScannedAt)
                val newSongs = mediaStoreQuery.query(context)
                dispatch(SongsReducer.SongsAction.UpdateLastScan(System.currentTimeMillis()))
                dispatch(SongsReducer.SongsAction.UpdateSongs(newSongs))
            }
        }
    }
}
