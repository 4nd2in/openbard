package ch.openbard.app

import android.app.Application
import android.util.Log
import ch.openbard.app.redux.AppState
import ch.openbard.app.redux.middlewares.LoggerMiddleware
import ch.openbard.app.redux.reducers.NavigationReducer
import ch.smoca.redux.Store
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.io.File

class MainApplication : Application() {
    lateinit var store: Store<AppState>

    override fun onCreate() {
        super.onCreate()
        store =
            Store(
                initialState = loadState(),
                reducers = listOf(
                    NavigationReducer()
                ),
                sagas = listOf(),
                middlewares =
                    listOf(
                        LoggerMiddleware(),
                    ),
            )
    }

    private fun loadState(): AppState {
        val file = File(filesDir, AppState.FILE_NAME)
        return if (file.exists()) {
            file.bufferedReader().use {
                try {
                    Json.decodeFromString<AppState>(it.readText())
                } catch (e: SerializationException) {
                    Log.e("Redux", "Could not serialize state file: ${e.message}")
                    file.delete()
                    AppState()
                } catch (e: IllegalArgumentException) {
                    Log.e("Redux", "Could not serialize state file: ${e.message}")
                    file.delete()
                    AppState()
                }
            }
        } else {
            AppState()
        }
    }
}
