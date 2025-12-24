package ch.openbard.app.redux.reducers

import ch.openbard.app.redux.AppState
import ch.smoca.redux.Action
import ch.smoca.redux.Reducer

class FavouritesReducer : Reducer<AppState> {
    sealed class FavouritesAction : Action {
        data class AddToFavourites(
            val songId: Long,
        ) : FavouritesAction()

        data class RemoveFromFavourites(
            val songId: Long,
        ) : FavouritesAction()
    }

    override fun reduce(
        action: Action,
        state: AppState,
    ): AppState {
        if (action !is FavouritesAction) return state

        val favourites = state.favourites.toMutableSet()

        when (action) {
            is FavouritesAction.AddToFavourites -> favourites.add(action.songId)
            is FavouritesAction.RemoveFromFavourites -> favourites.remove(action.songId)
        }

        return state.copy(favourites = favourites)
    }
}
