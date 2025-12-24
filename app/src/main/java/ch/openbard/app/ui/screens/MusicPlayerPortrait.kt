package ch.openbard.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import ch.openbard.app.redux.Player
import ch.openbard.app.redux.Song
import ch.openbard.app.ui.composables.AlbumArt
import ch.openbard.app.ui.composables.MusicSlider
import ch.openbard.app.ui.composables.PlaybackControls
import ch.openbard.app.ui.composables.QueueControls
import ch.openbard.app.ui.composables.SongInfo

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun MusicPlayerPortrait(
    song: Song,
    player: Player,
    isFavourite: Boolean,
    onPlayPause: () -> Unit = {},
    onSeek: (whereTo: Long) -> Unit = {},
    onNext: () -> Unit = {},
    onPrevious: () -> Unit = {},
    onShuffle: (Boolean) -> Unit = {},
    onRepeat: (Boolean) -> Unit = {},
    onFavourite: (Boolean) -> Unit = {},
) {
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
            uri = song.artworkUrl,
        )

        SongInfo(song.title, song.artist)

        Spacer(modifier = Modifier.height(16.dp))

        MusicSlider(
            progress = player.currentlyPlayingSongProgress,
            duration = song.duration ?: 0,
            onSeek = onSeek,
        )

        Spacer(modifier = Modifier.height(16.dp))

        PlaybackControls(player.isPlaying, onPlayPause, onNext, onPrevious)

        Spacer(modifier = Modifier.height(32.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            QueueControls(
                isShuffleOn = player.isShuffleOn,
                isRepeatOn = player.isRepeatOn,
                isFavourite = isFavourite,
                onShuffle = onShuffle,
                onRepeat = onRepeat,
                onFavourite = onFavourite,
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
@Preview(showSystemUi = true, showBackground = true)
@PreviewScreenSizes
fun MusicPlayerPortraitPreview() {
    MusicPlayerPortrait(
        song =
            Song(
                title = "Test Song",
                artist = "Test Artist",
                sourceUrl = "http://example.com",
            ),
        player = Player(),
        isFavourite = true,
    )
}
