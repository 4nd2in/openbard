package ch.openbard.app

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import ch.openbard.app.redux.AppState
import ch.openbard.app.redux.reducers.PermissionsReducer
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

    private val canReadAudio: Boolean
        get() =
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_MEDIA_AUDIO,
            ) == PackageManager.PERMISSION_GRANTED

    private val permissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            store.dispatch(PermissionsReducer.PermissionsAction.UpdateReadMediaAudio(granted))
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        store = (application as MainApplication).store

        enableEdgeToEdge()
        setContent {
            val state by store.stateObservable.collectAsState()

            LaunchedEffect(state.permissions.canReadMediaAudio) {
                if (state.permissions.canReadMediaAudio) {
                    store.dispatch(SongsFinderSaga.SongsFinderAction.Query)
                } else {
                    permissionLauncher.launch(Manifest.permission.READ_MEDIA_AUDIO)
                }
            }

            OpenBardTheme {
                AppNavigation(state, store::dispatch)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        startDurationUpdateJob()
        checkAndUpdatePermissions()
    }

    override fun onPause() {
        super.onPause()
        stopDurationUpdateJob()
    }

    override fun onDestroy() {
        super.onDestroy()
        store.dispatch(PlayerSaga.PlayerAction.Release)
    }

    @Suppress("MagicNumber")
    private fun startDurationUpdateJob() {
        playerUpdateJob =
            lifecycleScope.launch(Dispatchers.IO) {
                while (true) {
                    if (store.getState().player.isPlaying) {
                        store.dispatch(PlayerSaga.PlayerAction.UpdatePosition)
                    }
                    delay(200L)
                }
            }
    }

    private fun stopDurationUpdateJob() {
        if (::playerUpdateJob.isInitialized) playerUpdateJob.cancel()
    }

    private fun checkAndUpdatePermissions() {
        store.dispatch(PermissionsReducer.PermissionsAction.UpdateReadMediaAudio(canReadAudio))
    }
}
