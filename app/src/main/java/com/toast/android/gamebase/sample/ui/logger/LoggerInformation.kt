package com.toast.android.gamebase.sample.ui.logger

import androidx.compose.runtime.MutableState

data class LoggerInformation(
    val loggerLevel: MutableState<Int>,
    val loggerMessage: MutableState<String>,
    val loggerUserKey: MutableState<String>,
    val loggerUserValue: MutableState<String>
) {
    fun refreshData() {
        loggerLevel.value = 0
        loggerMessage.value = ""
        loggerUserKey.value = ""
        loggerUserValue.value = ""
    }
}