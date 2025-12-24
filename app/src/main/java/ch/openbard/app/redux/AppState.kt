package ch.openbard.app.redux

import androidx.compose.runtime.Immutable
import androidx.core.net.toUri
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import ch.openbard.app.ui.navigation.BackStackEntry
import ch.smoca.redux.State
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Immutable
@Serializable
data class AppState(
    val songs: Map<Long, Song> = emptyMap(),
    val favourites: Set<Long> = emptySet(),
    val lastScannedAt: Long = 0,
    @Transient
    val navigation: AppNavigation = AppNavigation(),
    @Transient
    val player: Player = Player(),
    @Transient
    val permissions: Permissions = Permissions(),
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

fun Map.Entry<Long, Song>.toMediaItem(): MediaItem =
    MediaItem
        .Builder()
        .setUri(value.sourceUrl)
        .setMediaId(key.toString())
        .setMediaMetadata(
            MediaMetadata
                .Builder()
                .setTitle(value.title)
                .setArtist(value.artist)
                .setAlbumTitle(value.album)
                .setArtworkUri(value.artworkUrl?.toUri())
                .build(),
        ).build()

@Immutable
data class Player(
    val isInitialized: Boolean = false,
    val isPlaying: Boolean = false,
    val isRepeatOn: Boolean = false,
    val isShuffleOn: Boolean = false,
    val currentPlaylist: List<Long> = emptyList(),
    val currentlyPlayingSongId: Long? = null,
    val currentlyPlayingSongProgress: Long = 0,
)

@Immutable
data class Permissions(
    val canReadMediaAudio: Boolean = false,
)
