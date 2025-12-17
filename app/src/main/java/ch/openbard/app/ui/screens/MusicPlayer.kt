package ch.openbard.app.ui.screens

import android.content.res.Configuration
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun MusicPlayer(isPlaying: Boolean) {
    if (isLandscape()) {
        MusicPlayerLandscape(isPlaying)
    } else {
        MusicPlayerPortrait(isPlaying)
    }
}

@Composable
private fun isLandscape(): Boolean {
    return LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE
}
