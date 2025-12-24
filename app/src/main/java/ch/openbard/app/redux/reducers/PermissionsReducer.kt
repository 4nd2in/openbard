package ch.openbard.app.redux.reducers

import ch.openbard.app.redux.AppState
import ch.smoca.redux.Action
import ch.smoca.redux.Reducer

class PermissionsReducer : Reducer<AppState> {
    sealed class PermissionsAction : Action {
        data class UpdateReadMediaAudio(
            val isGranted: Boolean,
        ) : PermissionsAction()
    }

    override fun reduce(
        action: Action,
        state: AppState,
    ): AppState {
        if (action !is PermissionsAction) return state

        return when (action) {
            is PermissionsAction.UpdateReadMediaAudio ->
                state.copy(
                    permissions = state.permissions.copy(canReadMediaAudio = action.isGranted),
                )
        }
    }
}
