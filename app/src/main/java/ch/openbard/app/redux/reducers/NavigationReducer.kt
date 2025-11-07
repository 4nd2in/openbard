package ch.openbard.app.redux.reducers

import ch.openbard.app.redux.AppState
import ch.openbard.app.ui.navigation.BackStackEntry
import ch.smoca.redux.Action
import ch.smoca.redux.Reducer

class NavigationReducer : Reducer<AppState> {
    sealed class NavigationAction : Action {
        data class UpdateBackStack(val backStack: List<BackStackEntry>) : NavigationAction()
        data class PushToBackStack(val backStackEntry: BackStackEntry) : NavigationAction()
        data object PopBackStack : NavigationAction()
    }

    override fun reduce(
        action: Action,
        state: AppState,
    ): AppState {
        if (action !is NavigationAction) return state

        return when (action) {
            is NavigationAction.UpdateBackStack -> {
                state.copy(navigation = state.navigation.copy(backStack = action.backStack))
            }

            is NavigationAction.PushToBackStack -> {
                val backStack = state.navigation.backStack.toMutableList()
                if (backStack.lastOrNull() != action.backStackEntry) {
                    backStack.addLast(action.backStackEntry)
                }
                state.copy(navigation = state.navigation.copy(backStack = backStack))
            }
            is NavigationAction.PopBackStack -> {
                val backStack = state.navigation.backStack.toMutableList()
                backStack.removeLastOrNull()
                state.copy(navigation = state.navigation.copy(backStack = backStack))
            }
        }
    }
}
