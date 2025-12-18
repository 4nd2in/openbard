package ch.openbard.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import ch.openbard.app.R
import ch.openbard.app.ui.composables.AlbumArt
import ch.openbard.app.ui.composables.PlaybackControls
import ch.openbard.app.ui.composables.QueueControls
import ch.openbard.app.ui.composables.SongInfo

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun MusicPlayerPortrait(
    isPlaying: Boolean,
    onPlayPause: () -> Unit = {},
) {
    var isPlaying by remember { mutableStateOf(isPlaying) }
    var progress by remember { mutableFloatStateOf(0f) }

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        AlbumArt(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f),
            painter = painterResource(id = R.drawable.ic_image),
        )

        Spacer(modifier = Modifier.height(16.dp))

        SongInfo("Title", "Artist")

        Spacer(modifier = Modifier.height(16.dp))

        Slider(
            value = progress,
            onValueChange = { progress = it },
        )

        PlaybackControls(isPlaying, onPlayPause)

        QueueControls()
    }
}

@Composable
@Preview(showSystemUi = true, showBackground = true)
@PreviewScreenSizes
fun MusicPlayerPortraitPreview() {
    MusicPlayerPortrait(isPlaying = true)
}
