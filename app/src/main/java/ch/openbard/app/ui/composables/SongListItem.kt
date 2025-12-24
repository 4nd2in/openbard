package ch.openbard.app.ui.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewDynamicColors
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import ch.openbard.app.helper.TimeFormatter.formatDuration
import ch.openbard.app.redux.Song
import ch.openbard.app.ui.theme.OpenBardTheme
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun SongListItem(
    song: Song,
    onClick: () -> Unit = {},
) {
    ListItem(
        modifier =
            Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick),
        leadingContent = {
            AlbumArt(
                modifier =
                    Modifier
                        .size(56.dp)
                        .clip(RoundedCornerShape(12.dp)),
                uri = song.artworkUrl,
            )
        },
        headlineContent = {
            Text(
                text = song.title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        },
        supportingContent = {
            Text(
                text = song.artist,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        },
        trailingContent = {
            song.duration?.let { duration ->
                Text(
                    text = formatDuration(duration),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        },
    )
}

@Composable
@Preview
@PreviewLightDark
@PreviewDynamicColors
fun SongListItemPreview() {
    OpenBardTheme {
        SongListItem(
            Song(
                title = "Test Song",
                artist = "Test Artist",
                sourceUrl = "http://example.com",
                duration = 180000,
            ),
        )
    }
}
