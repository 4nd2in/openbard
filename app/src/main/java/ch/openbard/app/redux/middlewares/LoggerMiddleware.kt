package ch.openbard.app.redux.middlewares

import android.util.Log
import ch.openbard.app.BuildConfig
import ch.openbard.app.redux.AppState
import ch.smoca.redux.Action
import ch.smoca.redux.Middleware
import ch.smoca.redux.Store

class LoggerMiddleware : Middleware<AppState> {
    override fun process(
        action: Action,
        store: Store<AppState>,
        next: (action: Action) -> Unit,
    ) {
        if (BuildConfig.DEBUG) {
            Log.v("Redux", "${action.javaClass.simpleName}: $action")
            next(action)
        } else {
            next(action)
        }
    }
}