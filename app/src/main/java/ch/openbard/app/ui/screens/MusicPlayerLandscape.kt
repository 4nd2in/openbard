package ch.openbard.app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Slider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
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

@Composable
@Suppress("MagicNumber")
fun MusicPlayerLandscape(
    isPlaying: Boolean,
    onPlayPause: () -> Unit = {},
) {
    var isPlaying by remember { mutableStateOf(isPlaying) }
    var progress by remember { mutableFloatStateOf(0f) }

    Row(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AlbumArt(
            modifier =
                Modifier
                    .weight(1f)
                    .aspectRatio(1f),
            painter = painterResource(id = R.drawable.ic_image),
        )

        Spacer(Modifier.width(24.dp))

        Column(
            modifier = Modifier.weight(1.5f),
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            SongInfo("Title", "Artist")

            Slider(value = progress, onValueChange = {})

            PlaybackControls(isPlaying, onPlayPause)

            QueueControls()
        }
    }
}

@Composable
@Preview(showSystemUi = true, showBackground = true)
@PreviewScreenSizes
fun MusicPlayerLandscapePreview() {
    MusicPlayerLandscape(isPlaying = true)
}
