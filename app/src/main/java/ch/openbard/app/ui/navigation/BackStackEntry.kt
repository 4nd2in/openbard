package ch.openbard.app.ui.navigation

import androidx.navigation3.runtime.NavKey

sealed class BackStackEntry : NavKey {
    data object Home : BackStackEntry()

    data object Player : BackStackEntry()

    data object Settings : BackStackEntry()
}
