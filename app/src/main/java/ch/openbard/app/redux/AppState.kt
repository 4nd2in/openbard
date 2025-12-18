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
    val songs: List<Song> = listOf(
        Song(
            id = "1",
            title = "Test Song",
            artist = "Test Artist",
            sourceUrl = "http://example.com",
            durationMs = 180000,
        ),
    ),
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
    val id: String,
    val title: String,
    val artist: String,
    val sourceUrl: String,
    val album: String? = null,
    val artworkUrl: String? = null,
    val durationMs: Long? = null,
    val albumArtist: String? = null,
    val composer: String? = null,
    val genre: String? = null,
    val year: Int? = null,
    val trackNumber: Int? = null,
    val totalTracks: Int? = null,
    val discNumber: Int? = null,
    val totalDiscs: Int? = null,
    val bpm: Int? = null,
    val musicalKey: String? = null,
    val isrc: String? = null,
    val copyright: String? = null,
    val comment: String? = null,
)
