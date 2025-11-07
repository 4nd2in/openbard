package ch.openbard.app.ui.navigation

import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import ch.openbard.app.redux.AppState
import ch.openbard.app.redux.reducers.NavigationReducer
import ch.openbard.app.ui.screens.Home
import ch.openbard.app.ui.screens.MusicPlayer
import ch.openbard.app.ui.screens.Settings
import ch.smoca.redux.Action

@Composable
fun Navigation(state: AppState, dispatch: (Action) -> Unit) {
    if (state.navigation.shouldShowNavigationSuite) {
        NavigationSuiteScaffold(
            navigationSuiteItems = {
                NavigationSuiteItems.entries.forEach { suiteItems ->
                    item(
                        icon = {
                            Icon(
                                painterResource(suiteItems.icon),
                                contentDescription = stringResource(suiteItems.labelRes)
                            )
                        },
                        label = { Text(stringResource(suiteItems.labelRes)) },
                        selected = suiteItems.backStackEntry == state.navigation.currentBackStackEntry,
                        onClick = {
                            dispatch(
                                NavigationReducer.NavigationAction.PushToBackStack(
                                    backStackEntry = suiteItems.backStackEntry
                                )
                            )
                        }
                    )
                }
            },
        ) {
            Screens(state = state, dispatch = dispatch)
        }
    } else {
        Screens(state = state, dispatch = dispatch)
    }
}

@Composable
private fun Screens(state: AppState, dispatch: (Action) -> Unit) {
    NavDisplay(
        backStack = state.navigation.backStack,
        onBack = { dispatch(NavigationReducer.NavigationAction.PopBackStack) },
        entryProvider = entryProvider {
            entry<BackStackEntry.Home> {
                Home {
                    dispatch(
                        NavigationReducer.NavigationAction.UpdateBackStack(
                            backStack = listOf(BackStackEntry.Settings)
                        )
                    )
                }
            }
            entry<BackStackEntry.Player> { MusicPlayer() }
            entry<BackStackEntry.Settings> { Settings() }
        },
    )
}
