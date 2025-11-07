package ch.openbard.app.ui.navigation

sealed class BackStackEntry {
    data object Home : BackStackEntry()
    data object Player : BackStackEntry()
    data object Settings : BackStackEntry()
}
