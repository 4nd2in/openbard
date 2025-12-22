package ch.openbard.app.player

import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.database.ContentObserver
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import ch.openbard.app.redux.Song
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

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
            MediaStore.Audio.Media.ALBUM_ARTIST,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.YEAR,
            MediaStore.Audio.Media.TRACK,
            MediaStore.Audio.Media.GENRE,
        )

    private val queryArgs =
        Bundle().apply {
            putString(
                ContentResolver.QUERY_ARG_SQL_SELECTION,
                """
                ${MediaStore.Audio.Media.IS_MUSIC} != 0 AND
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
    suspend fun query(context: Context): List<Song> =
        withContext(Dispatchers.IO) {
            val songs = mutableListOf<Song>()
            context.contentResolver
                .query(
                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                    projection,
                    queryArgs,
                    null,
                )?.use { cursor ->

                    val idCol = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
                    val titleCol = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
                    val artistCol = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
                    val albumCol = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM)
                    val durationCol = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)
                    val yearCol = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.YEAR)
                    val trackCol = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TRACK)
                    val genreCol = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.GENRE)

                    while (cursor.moveToNext()) {
                        val id = cursor.getLong(idCol)
                        val contentUri =
                            ContentUris.withAppendedId(
                                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                                id,
                            )

                        val artworkUrl: String? = readArtwork(context, id, contentUri)

                        songs.add(
                            Song(
                                id = cursor.getLong(idCol),
                                title = cursor.getString(titleCol),
                                artist = cursor.getString(artistCol),
                                album = cursor.getString(albumCol),
                                duration = cursor.getLong(durationCol),
                                sourceUrl = contentUri.toString(),
                                year = cursor.getInt(yearCol),
                                trackNumber = cursor.getInt(trackCol),
                                genre = cursor.getString(genreCol),
                                artworkUrl = artworkUrl,
                            ),
                        )
                        Log.i("MediaQuery", "New Song: ${songs.last().title}")
                    }
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

    private fun readArtwork(context: Context, songId: Long, contentUri: Uri): String? {
        val retriever = MediaMetadataRetriever()

        return try {
            retriever.setDataSource(context, contentUri)
            retriever.embeddedPicture?.let {
                saveArtwork(context, songId, it)
            }
        } catch (e: IllegalArgumentException) {
            Log.e("${javaClass.simpleName}", "${e.message}")
            null
        } catch (e: SecurityException) {
            Log.e("${javaClass.simpleName}", "${e.message}")
            null
        } finally {
            retriever.release()
        }
    }

    private fun saveArtwork(
        context: Context,
        songId: Long,
        bytes: ByteArray,
    ): String {
        val file = File(context.cacheDir, "art_$songId.jpg")
        file.outputStream().use { it.write(bytes) }
        return file.toURI().toString()
    }
}
