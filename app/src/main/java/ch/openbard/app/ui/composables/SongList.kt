package ch.openbard.app.ui.composables

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewDynamicColors
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import ch.openbard.app.redux.Song
import ch.openbard.app.ui.theme.OpenBardTheme

@Composable
fun SongList(
    modifier: Modifier = Modifier,
    songs: List<Song>,
    onItemClick: (Song) -> Unit = {},
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(vertical = 8.dp),
    ) {
        items(
            items = songs,
            key = { it.id },
        ) { song ->
            SongListItem(
                song = song,
                onClick = { onItemClick(song) },
            )
        }
    }
}

@Composable
@Preview
@PreviewLightDark
@PreviewDynamicColors
fun SongListPreview() {
    OpenBardTheme {
        SongList(
            songs =
                listOf(
                    Song(
                        id = "1",
                        title = "Test Song 1",
                        artist = "Test Artist 1",
                        sourceUrl = "http://example.com",
                    ),
                    Song(
                        id = "2",
                        title = "Test Song 2",
                        artist = "Test Artist 2",
                        sourceUrl = "http://example.com",
                    ),
                ),
        )
    }
}
