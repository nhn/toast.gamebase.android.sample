package com.toast.android.gamebase.sample.ui.logger

import com.toast.android.gamebase.sample.gamebasemanager.*

interface LoggerLevel {
    fun sendLog(message: String, userField: Map<String?, String?>)
}

class Debug() : LoggerLevel {
    override fun sendLog(message: String, userField: Map<String?, String?>) {
        sendLogDebug(message, userField)
    }
}

class Info() : LoggerLevel {
    override fun sendLog(message: String, userField: Map<String?, String?>) {
        sendLogInfo(message, userField)
    }
}

class Warn() : LoggerLevel {
    override fun sendLog(message: String, userField: Map<String?, String?>) {
        sendLogWarn(message, userField)
    }
}

class Error() : LoggerLevel {
    override fun sendLog(message: String, userField: Map<String?, String?>) {
        sendLogError(message, userField)
    }
}

class Fatal() : LoggerLevel {
    override fun sendLog(message: String, userField: Map<String?, String?>) {
        sendLogFatal(message, userField)
    }
}