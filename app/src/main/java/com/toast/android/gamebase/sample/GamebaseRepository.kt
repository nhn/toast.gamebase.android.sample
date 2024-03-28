package com.toast.android.gamebase.sample

import android.app.Activity
import com.toast.android.gamebase.Gamebase
import com.toast.android.gamebase.sample.gamebase_manager.createGamebaseEventHandler

object GamebaseRepository {
    private var isGamebaseEventHandlerAdded = false

    fun addGamebaseEventHandler(activity: Activity, onKickOut: () -> Unit) {
        if (isGamebaseEventHandlerAdded) {
            return
        }
        val gamebaseEventHandler = createGamebaseEventHandler(activity, onKickOut)
        Gamebase.addEventHandler(gamebaseEventHandler)

        isGamebaseEventHandlerAdded = true
    }
}
