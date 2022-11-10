package com.toast.android.gamebase.sample.ui.developer.logger

import android.app.Activity
import android.content.Context
import com.toast.android.gamebase.sample.gamebasemanager.initializeNhnCloudLogger
import com.toast.android.gamebase.sample.gamebasemanager.setNhnCloudLoggerListener

class LoggerInitializeDialogStateHolder() {
    fun initializeLogger(activity: Activity, appKey: String) {
        val context = activity as Context
        initializeNhnCloudLogger(context, appKey)
        setNhnCloudLoggerListener()
    }
}