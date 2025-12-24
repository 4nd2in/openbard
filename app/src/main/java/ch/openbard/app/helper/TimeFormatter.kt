package ch.openbard.app.helper

import java.util.Locale

@Suppress("MagicNumber")
object TimeFormatter {
    fun formatDuration(durationMs: Long): String {
        val totalSeconds = durationMs / 1000
        val minutes = totalSeconds / 60
        val seconds = totalSeconds % 60
        return String.format(Locale.GERMAN, "%d:%02d", minutes, seconds)
    }
}
