package ch.openbard.app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Slider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import ch.openbard.app.redux.Song
import ch.openbard.app.ui.composables.AlbumArt
import ch.openbard.app.ui.composables.PlaybackControls
import ch.openbard.app.ui.composables.QueueControls
import ch.openbard.app.ui.composables.SongInfo

@Composable
@Suppress("MagicNumber")
fun MusicPlayerLandscape(
    song: Song,
    progress: Long = 0,
    isPlaying: Boolean,
    onPlayPause: () -> Unit = {},
    onSeek: (whereTo: Long) -> Unit = {},
    onNext: () -> Unit = {},
    onPrevious: () -> Unit = {},
) {
    var localProgress by remember(progress) { mutableFloatStateOf(progress.toFloat()) }

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
            uri = song.artworkUrl,
        )

        Spacer(Modifier.width(24.dp))

        Column(
            modifier = Modifier.widthIn(min = 350.dp, max = 500.dp),
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            SongInfo("Title", "Artist")

            Slider(
                value = localProgress,
                valueRange = 0f..(song.duration ?: 0).toFloat(),
                onValueChange = { localProgress = it },
                onValueChangeFinished = { onSeek(localProgress.toLong()) },
            )

            PlaybackControls(isPlaying, onPlayPause, onNext, onPrevious)

            QueueControls()
        }
    }
}

@Composable
@Preview(showSystemUi = true, showBackground = true)
@PreviewScreenSizes
fun MusicPlayerLandscapePreview() {
    MusicPlayerLandscape(
        song = Song(
            id = 1,
            title = "Test Song",
            artist = "Test Artist",
            sourceUrl = "http://example.com",
        ),
        isPlaying = true,
    )
}
