package ch.openbard.app.ui.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewDynamicColors
import ch.openbard.app.helper.TimeFormatter.formatDuration
import ch.openbard.app.ui.theme.OpenBardTheme
import kotlin.math.roundToLong

@Composable
fun MusicSlider(
    modifier: Modifier = Modifier,
    progress: Long,
    duration: Long,
    onSeek: (whereTo: Long) -> Unit = {},
) {
    var localProgress by remember { mutableFloatStateOf(progress.toFloat()) }
    var isSeeking by remember { mutableStateOf(false) }

    if (!isSeeking) {
        localProgress = progress.toFloat()
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        Slider(
            value = localProgress,
            valueRange = 0f..duration.toFloat(),
            onValueChange = {
                localProgress = it
                isSeeking = true
            },
            onValueChangeFinished = {
                onSeek(localProgress.roundToLong())
                isSeeking = false
            },
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = formatDuration(localProgress.roundToLong()),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Text(
                text = formatDuration(duration),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Preview
@PreviewDynamicColors
@Composable
fun MusicSliderPreview() {
    OpenBardTheme {
        Column {
            MusicSlider(progress = 0L, duration = 1000000L)
            MusicSlider(progress = 300000L, duration = 1000000L)
        }
    }
}
