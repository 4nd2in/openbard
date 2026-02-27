package ch.openbard.app.ui.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ButtonGroupDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.IconButtonShapes
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewDynamicColors
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import ch.openbard.app.R
import ch.openbard.app.ui.theme.OpenBardTheme

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun PlaybackControls(
    isPlaying: Boolean,
    onPlayPause: () -> Unit = {},
    onNext: () -> Unit = {},
    onPrevious: () -> Unit = {},
) {
    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconButton(
            modifier =
                Modifier
                    .padding(end = 8.dp)
                    .size(
                    IconButtonDefaults.largeContainerSize(
                        IconButtonDefaults.IconButtonWidthOption.Uniform,
                    ),
                ),
            shapes =
                IconButtonShapes(
                    shape = IconButtonDefaults.largeRoundShape,
                    pressedShape = IconButtonDefaults.largeSquareShape,
                ),
            colors = IconButtonDefaults.filledTonalIconButtonColors(),
            onClick = onPrevious,
        ) {
            Icon(
                painterResource(R.drawable.ic_previous),
                contentDescription = stringResource(R.string.previous),
            )
        }
        IconButton(
            modifier =
                Modifier.size(
                    IconButtonDefaults.largeContainerSize(
                        IconButtonDefaults.IconButtonWidthOption.Wide,
                    ),
                ),
            shapes =
                IconButtonShapes(
                    shape = IconButtonDefaults.largeSquareShape,
                    pressedShape = IconButtonDefaults.extraLargePressedShape,
                ),
            colors = IconButtonDefaults.filledIconButtonColors(),
            onClick = onPlayPause,
        ) {
            Icon(
                if (isPlaying) {
                    painterResource(R.drawable.ic_pause)
                } else {
                    painterResource(R.drawable.ic_play)
                },
                contentDescription =
                    if (isPlaying) {
                        stringResource(R.string.pause)
                    } else {
                        stringResource(R.string.play)
                    },
                modifier = Modifier.size(48.dp),
            )
        }
        IconButton(
            modifier =
                Modifier
                    .padding(start = 8.dp)
                    .size(
                    IconButtonDefaults.largeContainerSize(
                        IconButtonDefaults.IconButtonWidthOption.Uniform,
                    ),
                ),
            shapes =
                IconButtonShapes(
                    shape = IconButtonDefaults.largeRoundShape,
                    pressedShape = IconButtonDefaults.largeSquareShape,
                ),
            colors = IconButtonDefaults.filledTonalIconButtonColors(),
            onClick = onNext,
        ) {
            Icon(
                painterResource(R.drawable.ic_next),
                contentDescription = stringResource(R.string.next),
            )
        }
    }
}

@Composable
@Preview
@PreviewLightDark
@PreviewDynamicColors
fun PlaybackControlsPreview() {
    OpenBardTheme {
        PlaybackControls(isPlaying = false)
    }
}
