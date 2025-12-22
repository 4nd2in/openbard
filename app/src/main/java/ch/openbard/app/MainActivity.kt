package ch.openbard.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.lifecycleScope
import ch.openbard.app.redux.AppState
import ch.openbard.app.redux.sagas.PlayerSaga
import ch.openbard.app.redux.sagas.SongsFinderSaga
import ch.openbard.app.ui.navigation.AppNavigation
import ch.openbard.app.ui.theme.OpenBardTheme
import ch.smoca.redux.Store
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private lateinit var store: Store<AppState>
    private lateinit var playerUpdateJob: Job

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        store = (application as MainApplication).store

        store.dispatch(SongsFinderSaga.SongsFinderAction.Query)

        enableEdgeToEdge()
        setContent {
            val state by store.stateObservable.collectAsState()

            OpenBardTheme {
                AppNavigation(state, store::dispatch)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        playerUpdateJob = lifecycleScope.launch(Dispatchers.IO) {
            while (true) {
                if (store.getState().player.isPlaying) {
                    store.dispatch(PlayerSaga.PlayerAction.UpdatePosition)
                }
                delay(500L)
            }
        }
    }

    override fun onPause() {
        super.onPause()
        if (::playerUpdateJob.isInitialized) playerUpdateJob.cancel()
    }
}
