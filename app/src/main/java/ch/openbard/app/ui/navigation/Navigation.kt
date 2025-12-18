package ch.openbard.app.ui.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.recalculateWindowInsets
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.navigation3.ListDetailSceneStrategy
import androidx.compose.material3.adaptive.navigation3.rememberListDetailSceneStrategy
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
fun Navigation(state: AppState, dispatch: (Action) -> Unit = {}) {
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
        Screens(
            Modifier
                .fillMaxSize()
                .recalculateWindowInsets()
                .safeDrawingPadding(),
            state = state,
            dispatch = dispatch
        )
    }
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
private fun Screens(
    modifier: Modifier = Modifier,
    state: AppState,
    dispatch: (Action) -> Unit = {},
) {
    val listDetailStrategy = rememberListDetailSceneStrategy<BackStackEntry>()

    NavDisplay(
        modifier = modifier,
        backStack = state.navigation.backStack,
        sceneStrategy = listDetailStrategy,
        onBack = { dispatch(NavigationReducer.NavigationAction.PopBackStack) },
        entryProvider = entryProvider {
            entry<BackStackEntry.Home>(
                metadata = ListDetailSceneStrategy.listPane(
                    detailPlaceholder = {
                        Text("Play a song")
                    }
                )) {
                Home(listOf("song")) {
                    dispatch(
                        NavigationReducer.NavigationAction.PushToBackStack(BackStackEntry.Player)
                    )
                }
            }
            entry<BackStackEntry.Player>(metadata = ListDetailSceneStrategy.detailPane()) {
                MusicPlayer(
                    isPlaying = false
                )
            }
            entry<BackStackEntry.Settings> { Settings() }
        },
    )
}
