package ch.openbard.app.redux.reducers

import ch.openbard.app.redux.AppState
import ch.smoca.redux.Action
import ch.smoca.redux.Reducer

class PlayerReducer : Reducer<AppState> {
    sealed class PlayerAction : Action {
        data class UpdateIsInitialized(val isInitialized: Boolean) : PlayerAction()
        data class UpdateIsPlaying(val isPlaying: Boolean) : PlayerAction()
        data class UpdatePosition(val position: Long) : PlayerAction()
    }

    override fun reduce(
        action: Action,
        state: AppState,
    ): AppState {
        if (action !is PlayerAction) return state

        return when (action) {
            is PlayerAction.UpdateIsInitialized -> state.copy(player = state.player.copy(isInitialized = action.isInitialized))
            is PlayerAction.UpdateIsPlaying -> state.copy(player = state.player.copy(isPlaying = action.isPlaying))
            is PlayerAction.UpdatePosition -> state.copy(player = state.player.copy(position = action.position))
        }
    }
}
