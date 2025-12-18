package ch.openbard.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import ch.openbard.app.redux.AppState
import ch.openbard.app.ui.navigation.AppNavigation
import ch.openbard.app.ui.theme.OpenBardTheme
import ch.smoca.redux.Store

class MainActivity : ComponentActivity() {
    private lateinit var store: Store<AppState>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        store = (application as MainApplication).store

        enableEdgeToEdge()
        setContent {
            val state by store.stateObservable.collectAsState()

            OpenBardTheme {
                AppNavigation(state, store::dispatch)
            }
        }
    }
}
