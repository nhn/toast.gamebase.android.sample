package com.toast.android.gamebase.sample.ui.developer.logger

import android.app.Activity
import android.content.Context
import androidx.compose.runtime.mutableStateOf
import com.toast.android.gamebase.sample.gamebasemanager.initializeNhnCloudLogger
import com.toast.android.gamebase.sample.gamebasemanager.setNhnCloudLoggerListener

class LoggerInitializeDialogStateHolder() {
    fun initializeLogger(activity: Activity, appKey: String) {
        val context = activity as Context
        initializeNhnCloudLogger(context, appKey)
        setNhnCloudLoggerListener()
    }
}

class SendLogDialogStateHolder() {
    var loggerLevelExpanded = mutableStateOf(false)
    var loggerLevel = mutableStateOf(0)
    var loggerMessage = mutableStateOf("")
    var loggerUserKey = mutableStateOf("")
    var loggerUserValue = mutableStateOf("")

    fun sendLogger() {
        val userField = HashMap<String?, String?>()
        if (loggerUserKey.value.isNotEmpty() && loggerUserValue.value.isNotEmpty()) {
            userField[loggerUserKey.value] =
                loggerUserValue.value
        }
        getSendLoggerType(loggerLevel.value).sendLog(
            loggerMessage.value,
            userField
        )
    }

    private fun getSendLoggerType(loggerLevel: Int): LoggerLevel {
        return when (loggerLevel) {
            0 -> Debug()
            1 -> Info()
            2 -> Warn()
            3 -> Error()
            4 -> Fatal()
            else -> Debug()
        }
    }
}