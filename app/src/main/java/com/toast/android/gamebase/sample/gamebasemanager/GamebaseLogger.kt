package com.toast.android.gamebase.sample.gamebasemanager

import android.content.Context
import android.util.Log
import com.toast.android.gamebase.Gamebase
import com.toast.android.gamebase.base.GamebaseException
import com.toast.android.gamebase.toastlogger.LoggerConfiguration
import com.toast.android.gamebase.toastlogger.data.LogEntry
import com.toast.android.gamebase.toastlogger.data.LogFilter
import com.toast.android.gamebase.toastlogger.data.LoggerListener

// NHN Cloud Logger (Log & Crash)

// See https://docs.toast.com/en/Game/Gamebase/en/aos-logger/#initialize
// TODO: [Fix me] AppKey for the Log & Crash from NHN Cloud Console > Analytics > Log & Crash Search
const val LOG_AND_CRASH_APPKEY = ""
const val SEND_CRASH_LOG = true

fun initializeNhnCloudLogger(context: Context) {
    Log.d(TAG, "Initialize NHN Cloud Logger")
    val configBuilder: LoggerConfiguration.Builder = LoggerConfiguration.newBuilder(
        LOG_AND_CRASH_APPKEY,
        SEND_CRASH_LOG
    )
    Gamebase.Logger.initialize(context, configBuilder.build())
}

// 로그 레벨은 다음과 같습니다.
// DEBUG > INFO > WARN > ERROR > FATAL
fun sendLogDebug(message: String, userField: Map<String?, Any?>) {
    Gamebase.Logger.debug(message, userField)
}

fun sendLogInfo(message: String, userField: Map<String?, Any?>) {
    Gamebase.Logger.info(message, userField)
}

fun sendLogWarn(message: String, userField: Map<String?, Any?>) {
    Gamebase.Logger.warn(message, userField)
}

fun sendLogError(message: String, userField: Map<String?, Any?>) {
    Gamebase.Logger.error(message, userField)
}

fun sendLogFatal(message: String, userField: Map<String?, Any?>) {
    Gamebase.Logger.fatal(message, userField)
}

// 리스너(listener)를 등록하면 로그 전송 후 추가 작업을 진행할 수 있습니다.
fun setNhnCloudLoggerListener() {
    Gamebase.Logger.setLoggerListener(object : LoggerListener {
        override fun onSuccess(gbLogEntry: LogEntry) {}
        override fun onFilter(gbLogEntry: LogEntry, gbLogFilter: LogFilter) {
            // The logs were filtered out and not sent.
            Log.i(TAG, "SendLog.onFilter($gbLogEntry) : $gbLogFilter")
        }
        override fun onSave(gbLogEntry: LogEntry) {
            // If log transmission fails due to network disconnection, the log is saved in a file for log retransmission.(The saved file cannot be checked.)
            Log.i(TAG, "SendLog.onSave($gbLogEntry)")
        }
        override fun onError(gbLogEntry: LogEntry, exception: GamebaseException) {
            // Sending logs failed
            Log.w(TAG, "SendLog.onError($gbLogEntry) : $exception")
        }
    })
}
