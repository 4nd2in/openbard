package ch.openbard.app.ui.screens

import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.window.core.layout.WindowSizeClass.Companion.WIDTH_DP_EXPANDED_LOWER_BOUND

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun MusicPlayer(isPlaying: Boolean) {
    if (isScreenWide()) {
        MusicPlayerLandscape(isPlaying)
    } else {
        MusicPlayerPortrait(isPlaying)
    }
}

@Composable
private fun isScreenWide(): Boolean {
    val windowSize = currentWindowAdaptiveInfo().windowSizeClass
    return windowSize.isWidthAtLeastBreakpoint(WIDTH_DP_EXPANDED_LOWER_BOUND)
}

@Composable
@Preview(showSystemUi = true)
@PreviewScreenSizes
fun MusicPlayerPreview() {
    MusicPlayer(true)
}
