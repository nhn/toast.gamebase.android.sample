package com.toast.android.gamebase.sample.ui.developer.logger

import androidx.compose.runtime.mutableStateOf
import com.toast.android.gamebase.sample.gamebase_manager.sendReport

class SendReportDialogStateHolder {
    var loggerMessage = mutableStateOf("")
    var loggerUserKey = mutableStateOf("")
    var loggerUserValue = mutableStateOf("")

    fun sendReport() {
        val userField = HashMap<String?, String?>()
        if (loggerUserKey.value.isNotEmpty() && loggerUserValue.value.isNotEmpty()) {
            userField[loggerUserKey.value] =
                loggerUserValue.value
        }

        val testException = Exception("Test Exception!")
        sendReport(loggerMessage.value, testException, userField)
    }
}