package ch.openbard.app.ui.composables

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MultiChoiceSegmentedButtonRow
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewDynamicColors
import androidx.compose.ui.tooling.preview.PreviewLightDark
import ch.openbard.app.R
import ch.openbard.app.ui.theme.OpenBardTheme

@Composable
fun QueueControls(
    isShuffleOn: Boolean,
    isRepeatOn: Boolean,
    isFavourite: Boolean,
    onShuffle: (Boolean) -> Unit = {},
    onRepeat: (Boolean) -> Unit = {},
    onFavourite: (Boolean) -> Unit = {},
) {
    MultiChoiceSegmentedButtonRow {
        SegmentedButton(
            checked = isShuffleOn,
            onCheckedChange = {
                onShuffle(it)
            },
            shape =
                SegmentedButtonDefaults.itemShape(
                    index = 0,
                    count = 3,
                ),
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_shuffle),
                contentDescription = stringResource(R.string.shuffle),
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }

        SegmentedButton(
            checked = isRepeatOn,
            onCheckedChange = {
                onRepeat(it)
            },
            shape =
                SegmentedButtonDefaults.itemShape(
                    index = 1,
                    count = 3,
                ),
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_repeat),
                contentDescription = stringResource(R.string.repeat),
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }

        SegmentedButton(
            checked = isFavourite,
            onCheckedChange = {
                onFavourite(it)
            },
            shape =
                SegmentedButtonDefaults.itemShape(
                    index = 2,
                    count = 3,
                ),
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_favourite),
                contentDescription = stringResource(R.string.add_favourite),
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
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
        QueueControls(isShuffleOn = false, isRepeatOn = true, isFavourite = true)
    }
}
