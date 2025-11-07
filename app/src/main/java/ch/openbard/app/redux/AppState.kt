package ch.openbard.app.redux

import androidx.compose.runtime.Immutable
import ch.openbard.app.ui.navigation.BackStackEntry
import ch.smoca.redux.State
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Immutable
@Serializable
data class AppState(
    @Transient
    val navigation: AppNavigation = AppNavigation()
): State {
    companion object {
        const val FILE_NAME = "state.json"
    }
}

data class AppNavigation(
    val backStack: List<BackStackEntry> = listOf(BackStackEntry.Home)
) {
    companion object {
        val topLevelBackStackEntries = listOf(BackStackEntry.Home, BackStackEntry.Player)
    }

    val currentBackStackEntry get() = backStack.lastOrNull()
    val shouldShowNavigationSuite get() = currentBackStackEntry in topLevelBackStackEntries
}
