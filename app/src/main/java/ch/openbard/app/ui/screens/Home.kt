package ch.openbard.app.ui.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewDynamicColors
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import ch.openbard.app.ui.composables.SongList

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun Home(songs: List<String>, navigate: (String) -> Unit = {}) {
    SongList(Modifier.fillMaxSize(), songs, onItemClick = { navigate(it) })
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
@PreviewLightDark
@PreviewDynamicColors
@PreviewScreenSizes
fun HomePreview() {
    Home(listOf("Test"))
}
