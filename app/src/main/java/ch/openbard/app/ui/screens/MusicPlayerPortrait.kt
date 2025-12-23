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
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import ch.openbard.app.redux.Song
import ch.openbard.app.ui.composables.AlbumArt
import ch.openbard.app.ui.composables.PlaybackControls
import ch.openbard.app.ui.composables.QueueControls
import ch.openbard.app.ui.composables.SongInfo

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun MusicPlayerPortrait(
    song: Song,
    progress: Long = 0,
    isPlaying: Boolean,
    onPlayPause: () -> Unit = {},
    onSeek: (whereTo: Long) -> Unit = {},
    onNext: () -> Unit = {},
    onPrevious: () -> Unit = {},
) {
    var localProgress by remember(progress) { mutableFloatStateOf(progress.toFloat()) }

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
                    .weight(1f)
                    .aspectRatio(1f),
            uri = song.artworkUrl
        )

        Spacer(modifier = Modifier.height(16.dp))

        SongInfo(song.title, song.artist)

        Spacer(modifier = Modifier.height(16.dp))

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

@Composable
@Preview(showSystemUi = true, showBackground = true)
@PreviewScreenSizes
fun MusicPlayerPortraitPreview() {
    MusicPlayerPortrait(
        song = Song(
            title = "Test Song",
            artist = "Test Artist",
            sourceUrl = "http://example.com",
        ),
        isPlaying = true,
    )
}
