package ch.openbard.app.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ch.openbard.app.R
import ch.openbard.app.ui.theme.OpenBardTheme

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun MusicPlayer() {
    var isPlaying by remember { mutableStateOf(false) }
    var progress by remember { mutableFloatStateOf(0.4f) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.surface),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_image),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Song Title",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Artist Name",
                fontSize = 16.sp
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Slider(
            value = progress,
            onValueChange = { progress = it },
        )

        Row(
            Modifier
                .padding(horizontal = 8.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(
                modifier =
                    Modifier.size(
                        IconButtonDefaults.mediumContainerSize(
                            IconButtonDefaults.IconButtonWidthOption.Wide
                        )
                    ),
                shape = IconButtonDefaults.mediumRoundShape,
                colors = IconButtonDefaults.filledIconButtonColors(),
                onClick = { /* Previous */ }
            ) {
                Icon(
                    painterResource(R.drawable.ic_previous),
                    contentDescription = stringResource(R.string.previous)
                )
            }
            IconButton(
                modifier =
                    Modifier.size(
                        IconButtonDefaults.largeContainerSize(
                            IconButtonDefaults.IconButtonWidthOption.Wide
                        )
                    ),
                shape = IconButtonDefaults.largeRoundShape,
                colors = IconButtonDefaults.filledIconButtonColors(),
                onClick = { isPlaying = !isPlaying }
            ) {
                Icon(
                    if (isPlaying) painterResource(R.drawable.ic_pause) else painterResource(R.drawable.ic_play),
                    contentDescription = if (isPlaying) stringResource(R.string.pause) else stringResource(
                        R.string.play
                    ),
                    modifier = Modifier.size(48.dp)
                )
            }
            IconButton(
                modifier =
                    Modifier.size(
                        IconButtonDefaults.mediumContainerSize(
                            IconButtonDefaults.IconButtonWidthOption.Wide
                        )
                    ),
                shape = IconButtonDefaults.mediumRoundShape,
                colors = IconButtonDefaults.filledIconButtonColors(),
                onClick = { /* Next */ }
            ) {
                Icon(
                    painterResource(R.drawable.ic_next),
                    contentDescription = stringResource(R.string.next)
                )
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            IconButton(onClick = { /* Shuffle */ }) {
                Icon(
                    painterResource(R.drawable.ic_shuffle),
                    contentDescription = stringResource(R.string.shuffle)
                )
            }
            IconButton(onClick = { /* Repeat */ }) {
                Icon(
                    painterResource(R.drawable.ic_repeat),
                    contentDescription = stringResource(R.string.repeat)
                )
            }
            IconButton(onClick = { /* Queue */ }) {
                Icon(
                    painterResource(R.drawable.ic_queue_music),
                    contentDescription = stringResource(R.string.queue)
                )
            }
        }
    }
}

@Composable
@Preview(showSystemUi = true, showBackground = true)
fun MusicPlayerPreview() {
    OpenBardTheme(darkTheme = false) {
        MusicPlayer()
    }
}
