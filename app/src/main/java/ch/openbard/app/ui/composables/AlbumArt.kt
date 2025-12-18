package ch.openbard.app.ui.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewDynamicColors
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import ch.openbard.app.R
import ch.openbard.app.ui.theme.OpenBardTheme

@Composable
fun AlbumArt(
    modifier: Modifier = Modifier,
    painter: Painter,
) {
    Box(
        modifier =
            modifier
                .clip(RoundedCornerShape(32.dp))
                .background(MaterialTheme.colorScheme.secondary),
        contentAlignment = Alignment.Center,
    ) {
        Image(
            modifier = Modifier.fillMaxSize(),
            painter = painter,
            contentDescription = null,
            contentScale = ContentScale.Crop,
        )
    }
}

@Composable
@Preview
@PreviewLightDark
@PreviewDynamicColors
fun AlbumArtPreview() {
    OpenBardTheme {
        AlbumArt(
            modifier = Modifier.aspectRatio(1f),
            painter = painterResource(id = R.drawable.ic_image),
        )
    }
}
