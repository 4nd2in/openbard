package ch.openbard.app.player

import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.database.ContentObserver
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import ch.openbard.app.redux.Song
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MediaStoreQuery(
    lastScannedAt: Long,
) {
    @Suppress("MagicNumber")
    private val lastScannedAtSeconds = lastScannedAt / 1000

    private val projection =
        arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.ALBUM_ID,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.YEAR,
            MediaStore.Audio.Media.TRACK,
            MediaStore.Audio.Media.GENRE,
        )

    // do not query music files shorter than 30s
    private val queryArgs =
        Bundle().apply {
            putString(
                ContentResolver.QUERY_ARG_SQL_SELECTION,
                """
                ${MediaStore.Audio.Media.IS_MUSIC} != 0 AND
                ${MediaStore.Audio.Media.DURATION} >= 30000 AND 
                (${MediaStore.Audio.Media.DATE_ADDED} > ? OR
                 ${MediaStore.Audio.Media.DATE_MODIFIED} > ?)
                """.trimIndent(),
            )

            putStringArray(
                ContentResolver.QUERY_ARG_SQL_SELECTION_ARGS,
                arrayOf(
                    lastScannedAtSeconds.toString(),
                    lastScannedAtSeconds.toString(),
                ),
            )

            putString(
                ContentResolver.QUERY_ARG_SQL_SORT_ORDER,
                "${MediaStore.Audio.Media.DATE_MODIFIED} DESC",
            )
        }

    @Suppress("LongMethod")
    suspend fun query(context: Context): Map<Long, Song> =
        withContext(Dispatchers.IO) {
            val songs = mutableMapOf<Long, Song>()

            val collection = MediaStore.Audio.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)

            try {
                context.contentResolver.query(
                    collection,
                    projection,
                    queryArgs,
                    null,
                )?.use { cursor ->
                    val idCol = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
                    val titleCol = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
                    val artistCol = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
                    val albumCol = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM)
                    val albumIdCol = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID)
                    val durationCol = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)
                    val yearCol = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.YEAR)
                    val trackCol = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TRACK)
                    val genreCol = cursor.getColumnIndex(MediaStore.Audio.Media.GENRE)

                    while (cursor.moveToNext()) {
                        val id = cursor.getLong(idCol)
                        val albumId = cursor.getLong(albumIdCol)

                        val contentUri = ContentUris.withAppendedId(collection, id)
                        val artworkUri = getAlbumArtUri(albumId)

                        songs[id] =
                            Song(
                                title = cursor.getString(titleCol) ?: "<unknown>",
                                artist = cursor.getString(artistCol) ?: "<unknown>",
                                album = cursor.getString(albumCol) ?: "<unknown>",
                                duration = cursor.getLong(durationCol),
                                sourceUrl = contentUri.toString(),
                                year = cursor.getInt(yearCol),
                                trackNumber = cursor.getInt(trackCol),
                                genre = if (genreCol != -1) cursor.getString(genreCol) else null,
                                artworkUrl = artworkUri.toString(),
                            )
                    }
                }
            } catch (e: Exception) {
                Log.e("MediaQuery", "Error querying music", e)
            }
            songs
        }

    fun registerObserver(
        context: Context,
        observer: ContentObserver,
    ) {
        context.contentResolver.registerContentObserver(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            true,
            observer,
        )
    }

    fun unregisterObserver(
        context: Context,
        observer: ContentObserver,
    ) {
        context.contentResolver.unregisterContentObserver(observer)
    }

    private fun getAlbumArtUri(albumId: Long): Uri {
        return ContentUris.withAppendedId(
            Uri.parse("content://media/external/audio/albumart"),
            albumId
        )
    }
}
