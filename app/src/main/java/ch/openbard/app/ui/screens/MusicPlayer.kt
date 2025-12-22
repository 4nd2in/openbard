package ch.openbard.app.ui.screens

import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.window.core.layout.WindowSizeClass.Companion.WIDTH_DP_EXPANDED_LOWER_BOUND
import ch.openbard.app.redux.Song

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun MusicPlayer(
    song: Song,
    isPlaying: Boolean,
    progress: Long,
    onPlayPause: () -> Unit = {},
    onSeek: (whereTo: Long) -> Unit = {},
    onNext: () -> Unit = {},
    onPrevious: () -> Unit = {},
) {
    if (isScreenWide()) {
        MusicPlayerLandscape(song, progress, isPlaying, onPlayPause, onSeek, onNext, onPrevious)
    } else {
        MusicPlayerPortrait(song, progress, isPlaying, onPlayPause, onSeek, onNext, onPrevious)
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
    MusicPlayer(
        song = Song(
            id = 1,
            title = "Test Song",
            artist = "Test Artist",
            sourceUrl = "http://example.com",
        ),
        progress = 0,
        isPlaying = true,
    )
}
