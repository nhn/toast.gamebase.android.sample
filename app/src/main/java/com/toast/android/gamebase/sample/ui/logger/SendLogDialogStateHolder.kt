package com.toast.android.gamebase.sample.ui.logger

import androidx.compose.runtime.mutableStateOf

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