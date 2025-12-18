package ch.openbard.app.ui.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import ch.openbard.app.R

enum class NavigationSuiteItems(
    @StringRes
    val labelRes: Int,
    @DrawableRes
    val icon: Int,
    val backStackEntry: BackStackEntry,
) {
    HOME(R.string.home, R.drawable.ic_home, BackStackEntry.Home),
    SETTINGS(R.string.settings, R.drawable.ic_settings, BackStackEntry.Settings),
}
