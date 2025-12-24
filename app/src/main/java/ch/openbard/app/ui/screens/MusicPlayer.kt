package ch.openbard.app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.window.core.layout.WindowSizeClass.Companion.WIDTH_DP_EXPANDED_LOWER_BOUND
import ch.openbard.app.redux.AppState
import ch.openbard.app.redux.Song
import ch.openbard.app.redux.reducers.FavouritesReducer
import ch.openbard.app.redux.sagas.PlayerSaga
import ch.smoca.redux.Action
import kotlin.collections.get

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun MusicPlayer(
    state: AppState,
    dispatch: (Action) -> Unit = {},
) {
    val song = state.songs[state.player.currentlyPlayingSongId]
    val isFavourite = state.player.currentlyPlayingSongId in state.favourites
    val onPlayPause = {
        if (state.player.isPlaying)
            dispatch(PlayerSaga.PlayerAction.Pause)
        else
            dispatch(PlayerSaga.PlayerAction.Play)
    }
    val onSeek: (Long) -> Unit = { dispatch(PlayerSaga.PlayerAction.SeekTo(it)) }
    val onNext = { dispatch(PlayerSaga.PlayerAction.SeekToNext) }
    val onPrevious = { dispatch(PlayerSaga.PlayerAction.SeekToPrevious) }
    val onShuffle: (Boolean) -> Unit = { dispatch(PlayerSaga.PlayerAction.SetShuffleMode(it)) }
    val onRepeat: (Boolean) -> Unit = { dispatch(PlayerSaga.PlayerAction.SetRepeatMode(it)) }
    val onFavourite: (Boolean) -> Unit = {
        state.player.currentlyPlayingSongId?.let { songId ->
            if (it) {
                dispatch(FavouritesReducer.FavouritesAction.AddToFavourites(songId))
            } else {
                dispatch(FavouritesReducer.FavouritesAction.RemoveFromFavourites(songId))
            }
        }
    }

    if (song == null) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            CircularProgressIndicator()
        }
    } else {
        if (isScreenWide()) {
            MusicPlayerLandscape(
                song,
                state.player,
                isFavourite,
                onPlayPause,
                onSeek,
                onNext,
                onPrevious,
                onShuffle,
                onRepeat,
                onFavourite,
            )
        } else {
            MusicPlayerPortrait(
                song,
                state.player,
                isFavourite,
                onPlayPause,
                onSeek,
                onNext,
                onPrevious,
                onShuffle,
                onRepeat,
                onFavourite,
            )
        }
    }
}

@Composable
private fun isScreenWide(): Boolean {
    val windowSize = currentWindowAdaptiveInfo().windowSizeClass
    return windowSize.isWidthAtLeastBreakpoint(WIDTH_DP_EXPANDED_LOWER_BOUND)
}

@Composable
@Preview(showSystemUi = true)
@PreviewScreenSizes
fun MusicPlayerPreview() {
    MusicPlayer(
        AppState(
            songs =
                mapOf(
                    1L to
                        Song(
                            title = "Test Song",
                            artist = "Test Artist",
                            sourceUrl = "http://example.com",
                        ),
                ),
        ),
    )
}
