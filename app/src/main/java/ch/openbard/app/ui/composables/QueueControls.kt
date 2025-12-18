package ch.openbard.app.ui.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewDynamicColors
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import ch.openbard.app.R
import ch.openbard.app.ui.theme.OpenBardTheme

@Composable
fun QueueControls(
    onShuffle: () -> Unit = {},
    onRepeat: () -> Unit = {},
    onQueue: () -> Unit = {},
) {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
    ) {
        IconButton(onClick = onShuffle) {
            Icon(
                painterResource(R.drawable.ic_shuffle),
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                contentDescription = stringResource(R.string.shuffle),
            )
        }
        IconButton(onClick = onRepeat) {
            Icon(
                painterResource(R.drawable.ic_repeat),
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                contentDescription = stringResource(R.string.repeat),
            )
        }
        IconButton(onClick = onQueue) {
            Icon(
                painterResource(R.drawable.ic_queue_music),
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                contentDescription = stringResource(R.string.queue),
            )
        }
    }
}

@Composable
@Preview
@PreviewLightDark
@PreviewDynamicColors
fun QueueControlsPreview() {
    OpenBardTheme {
        QueueControls()
    }
}
