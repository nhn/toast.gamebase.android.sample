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
// https://docs.toast.com/en/Game/Gamebase/en/aos-logger/#initialize

// TODO: [Fix me] AppKey for the Log & Crash from NHN Cloud Console > Analytics > Log & Crash Search
private const val LOG_AND_CRASH_APPKEY = ""
private const val SEND_CRASH_LOG = true

fun getAppKey() = LOG_AND_CRASH_APPKEY

fun initializeNhnCloudLogger(context: Context) {
    Log.d(TAG, "Initialize NHN Cloud Logger")
    val configBuilder: LoggerConfiguration.Builder = LoggerConfiguration.newBuilder(
        LOG_AND_CRASH_APPKEY,
        SEND_CRASH_LOG
    )
    Gamebase.Logger.initialize(context, configBuilder.build())
}

fun initializeNhnCloudLogger(context: Context, appKey: String) {
    Log.d(TAG, "Initialize NHN Cloud Logger")
    val configBuilder: LoggerConfiguration.Builder = LoggerConfiguration.newBuilder(
        appKey,
        SEND_CRASH_LOG
    )
    Gamebase.Logger.initialize(context, configBuilder.build())
}

// 로그 레벨은 다음과 같습니다.
// DEBUG > INFO > WARN > ERROR > FATAL
fun sendLogDebug(message: String, userField: Map<String?, String?>) {
    Gamebase.Logger.debug(message, userField)
}

fun sendLogInfo(message: String, userField: Map<String?, String?>) {
    Gamebase.Logger.info(message, userField)
}

fun sendLogWarn(message: String, userField: Map<String?, String?>) {
    Gamebase.Logger.warn(message, userField)
}

fun sendLogError(message: String, userField: Map<String?, String?>) {
    Gamebase.Logger.error(message, userField)
}

fun sendLogFatal(message: String, userField: Map<String?, String?>) {
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
