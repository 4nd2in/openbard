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
import ch.openbard.app.R
import ch.openbard.app.redux.AppState
import ch.openbard.app.redux.Song
import ch.openbard.app.redux.reducers.NavigationReducer
import ch.openbard.app.ui.screens.Home
import ch.openbard.app.ui.screens.MusicPlayer
import ch.openbard.app.ui.screens.Settings
import ch.smoca.redux.Action

@Composable
fun AppNavigation(
    state: AppState,
    dispatch: (Action) -> Unit,
    modifier: Modifier = Modifier,
) {
    NavigationSuiteScaffold(
        modifier = modifier,
        navigationSuiteItems = {
            NavigationSuiteItems.entries.forEach { suiteItem ->
                item(
                    icon = {
                        Icon(
                            painter = painterResource(suiteItem.icon),
                            contentDescription = stringResource(suiteItem.labelRes),
                        )
                    },
                    label = { Text(stringResource(suiteItem.labelRes)) },
                    selected = suiteItem.backStackEntry == state.navigation.currentBackStackEntry,
                    onClick = {
                        dispatch(
                            NavigationReducer.NavigationAction.PushToBackStack(
                                backStackEntry = suiteItem.backStackEntry,
                            ),
                        )
                    },
                )
            }
        },
    ) {
        Screens(
            songs = state.songs,
            backStack = state.navigation.backStack,
            onNavigate = { entry ->
                dispatch(NavigationReducer.NavigationAction.PushToBackStack(entry))
            },
            onPopBackStack = {
                dispatch(NavigationReducer.NavigationAction.PopBackStack)
            },
            modifier =
                Modifier
                    .fillMaxSize()
                    .recalculateWindowInsets()
                    .safeDrawingPadding(),
        )
    }
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
private fun Screens(
    songs: List<Song>,
    backStack: List<BackStackEntry>,
    onNavigate: (BackStackEntry) -> Unit,
    onPopBackStack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val listDetailStrategy = rememberListDetailSceneStrategy<BackStackEntry>()

    NavDisplay(
        modifier = modifier,
        backStack = backStack,
        sceneStrategy = listDetailStrategy,
        onBack = onPopBackStack,
        entryProvider =
            entryProvider {
                entry<BackStackEntry.Home>(
                    metadata =
                        ListDetailSceneStrategy.listPane(
                            detailPlaceholder = {
                                Text(stringResource(R.string.play_a_song))
                            },
                        ),
                ) {
                    Home(
                        songs = songs,
                        navigate = { _ -> onNavigate(BackStackEntry.Player) },
                    )
                }
                entry<BackStackEntry.Player>(
                    metadata = ListDetailSceneStrategy.detailPane(),
                ) {
                    MusicPlayer(isPlaying = false)
                }
                entry<BackStackEntry.Settings> {
                    Settings()
                }
            },
    )
}
