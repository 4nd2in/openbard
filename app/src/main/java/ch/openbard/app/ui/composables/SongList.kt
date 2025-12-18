package ch.openbard.app.ui.composables

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SongList(modifier: Modifier = Modifier, songs: List<String>, onItemClick: (String) -> Unit = {}) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(vertical = 8.dp)
    ) {
        items(
            items = songs,
            key = { it }
        ) { song ->
            SongListItem(
                song = song,
                onClick = { onItemClick(song) }
            )
        }
    }
}