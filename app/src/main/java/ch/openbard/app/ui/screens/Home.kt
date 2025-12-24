package ch.openbard.app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import ch.openbard.app.redux.Song
import ch.openbard.app.ui.composables.SongList

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun Home(
    songs: Map<Long, Song>,
    navigate: (Long) -> Unit = {},
) {
    if (songs.isEmpty()) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            CircularProgressIndicator()
        }
    }
    SongList(Modifier.fillMaxSize(), songs, onItemClick = { navigate(it) })
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
@PreviewScreenSizes
fun HomePreview() {
    Home(
        mapOf(
            1L to
                Song(
                    title = "Test Song",
                    artist = "Test Artist",
                    sourceUrl = "http://example.com",
                ),
        ),
    )
}
