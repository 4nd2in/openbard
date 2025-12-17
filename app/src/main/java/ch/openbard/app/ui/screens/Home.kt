package ch.openbard.app.ui.screens

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewDynamicColors
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import ch.openbard.app.ui.composables.SongListItem

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun Home(songs: List<String>, navigate: (String) -> Unit = {}) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 8.dp)
    ) {
        items(
            items = songs,
            key = { it }
        ) { song ->
            SongListItem(
                song = song,
                onClick = { navigate(song) }
            )
        }
    }
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
@PreviewLightDark
@PreviewDynamicColors
@PreviewScreenSizes
fun HomePreview() {
    Home(listOf("Test"))
}
