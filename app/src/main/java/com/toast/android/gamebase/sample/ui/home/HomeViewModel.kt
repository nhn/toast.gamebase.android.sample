package com.toast.android.gamebase.sample.ui.home

import android.app.Activity
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.toast.android.gamebase.sample.GamebaseRepository
import com.toast.android.gamebase.sample.gamebase_manager.getLaunchingInfo
import com.toast.android.gamebase.sample.util.getLastCheckedGameNoticeTime
import com.toast.android.gamebase.sample.util.saveLastCheckedGameNoticeTime

class HomeViewModel : ViewModel() {
    var isTestDevice by mutableStateOf(false)
    var matchingTypes by mutableStateOf("")
    var onKickOut by mutableStateOf(false)
    var hasNewGameNotice by mutableStateOf(false)

    fun setGamebaseEventHandler(activity: Activity) {
        GamebaseRepository.addGamebaseEventHandler(activity) {
            onKickOut = true
        }
    }

    fun getTestDeviceInfo() {
        val launchingInfo = getLaunchingInfo()
        isTestDevice = launchingInfo?.isTestDevice ?: false
        matchingTypes = launchingInfo?.testDeviceMatchingTypes?.joinToString(separator = " | ") ?: ""
    }

    fun checkNewGameNotice() {
        val latestNoticeTime = getLaunchingInfo()?.gameNoticeLatestNoticeTimeMillis ?: 0L
        val lastCheckedTime = getLastCheckedGameNoticeTime()
        hasNewGameNotice = latestNoticeTime > lastCheckedTime
    }

    fun markGameNoticeAsRead() {
        val latestNoticeTime = getLaunchingInfo()?.gameNoticeLatestNoticeTimeMillis ?: 0L
        if (latestNoticeTime > 0L) {
            saveLastCheckedGameNoticeTime(latestNoticeTime)
            hasNewGameNotice = false
        }
    }
}
