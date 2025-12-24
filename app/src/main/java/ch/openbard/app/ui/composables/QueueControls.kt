package ch.openbard.app.ui.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonGroupDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.ToggleButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewDynamicColors
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import ch.openbard.app.R
import ch.openbard.app.ui.theme.OpenBardTheme

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun QueueControls(
    isShuffleOn: Boolean,
    isRepeatOn: Boolean,
    isFavourite: Boolean,
    onShuffle: (Boolean) -> Unit = {},
    onRepeat: (Boolean) -> Unit = {},
    onFavourite: (Boolean) -> Unit = {},
) {
    val options = listOf("Shuffle", "Repeat", "Favourite")
    val checkedStates = listOf(isShuffleOn, isRepeatOn, isFavourite)
    val unCheckedIcons =
        listOf(
            painterResource(R.drawable.ic_shuffle),
            painterResource(R.drawable.ic_repeat),
            painterResource(R.drawable.ic_favourite),
        )
    val checkedIcons =
        listOf(
            painterResource(R.drawable.ic_shuffle),
            painterResource(R.drawable.ic_repeat),
            painterResource(R.drawable.ic_favourite_fill),
        )
    val onCheckedCallbacks = listOf(onShuffle, onRepeat, onFavourite)

    val modifiers = listOf(Modifier, Modifier, Modifier)

    Row(
        Modifier
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(ButtonGroupDefaults.ConnectedSpaceBetween),
    ) {
        options.forEachIndexed { index, label ->
            ToggleButton(
                checked = checkedStates[index],
                onCheckedChange = { onCheckedCallbacks[index](it) },
                modifier =
                    modifiers[index]
                        .weight(1f)
                        .semantics { role = Role.Checkbox },
                shapes =
                    when (index) {
                        0 -> ButtonGroupDefaults.connectedLeadingButtonShapes()
                        options.lastIndex -> ButtonGroupDefaults.connectedTrailingButtonShapes()
                        else -> ButtonGroupDefaults.connectedMiddleButtonShapes()
                    },
                contentPadding = PaddingValues(vertical = 16.dp),
            ) {
                Icon(
                    painter =
                        if (checkedStates[index]) {
                            checkedIcons[index]
                        } else {
                            unCheckedIcons[index]
                        },
                    contentDescription = label,
                )
            }
        }
    }
}

@Composable
@Preview
@PreviewLightDark
@PreviewDynamicColors
fun QueueControlsPreview() {
    OpenBardTheme {
        Column {
            QueueControls(isShuffleOn = false, isRepeatOn = false, isFavourite = false)
            Spacer(modifier = Modifier.height(16.dp))
            QueueControls(isShuffleOn = true, isRepeatOn = true, isFavourite = true)
        }
    }
}
