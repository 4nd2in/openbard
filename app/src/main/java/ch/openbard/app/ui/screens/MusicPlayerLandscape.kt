package ch.openbard.app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
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

@Composable
@Suppress("MagicNumber")
fun MusicPlayerLandscape(
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

            MusicSlider(
                progress = player.currentlyPlayingSongProgress,
                duration = song.duration ?: 0,
                onSeek = onSeek,
            )

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
        }
    }
}

@Composable
@Preview(showSystemUi = true, showBackground = true)
@PreviewScreenSizes
fun MusicPlayerLandscapePreview() {
    MusicPlayerLandscape(
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
