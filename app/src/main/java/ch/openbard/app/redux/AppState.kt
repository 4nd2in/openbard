package ch.openbard.app.redux

import androidx.compose.runtime.Immutable
import ch.openbard.app.ui.navigation.BackStackEntry
import ch.smoca.redux.State
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Immutable
@Serializable
data class AppState(
    @Transient
    val navigation: AppNavigation = AppNavigation(),
    val songs: List<Song> = emptyList(),
    val lastScannedAt: Long = 0,
    @Transient
    val player: Player = Player(),
) : State {
    companion object {
        const val FILE_NAME = "state.json"
    }
}

data class AppNavigation(
    val backStack: List<BackStackEntry> = listOf(BackStackEntry.Home),
) {
    val currentBackStackEntry get() = backStack.lastOrNull()
}

@Immutable
@Serializable
data class Song(
    val id: Long,
    val title: String,
    val artist: String,
    val sourceUrl: String,
    val album: String? = null,
    val artworkUrl: String? = null,
    val duration: Long? = null,
    val albumArtist: String? = null,
    val composer: String? = null,
    val genre: String? = null,
    val year: Int? = null,
    val trackNumber: Int? = null,
    val totalTracks: Int? = null,
)

@Immutable
data class Player(
    val isInitialized: Boolean = false,
    val isPlaying: Boolean = false,
    val position: Long = 0,
)
