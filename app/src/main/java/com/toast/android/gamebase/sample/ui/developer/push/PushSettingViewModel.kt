package com.toast.android.gamebase.sample.ui.developer.push

import android.app.Activity
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.toast.android.gamebase.Gamebase
import com.toast.android.gamebase.base.push.PushConfiguration
import com.toast.android.gamebase.base.push.data.GamebaseNotificationOptions
import com.toast.android.gamebase.sample.gamebasemanager.isSuccess
import com.toast.android.gamebase.sample.gamebasemanager.showAlert
import com.toast.android.gamebase.sample.util.printWithIndent

const val NOTIFICATION_PRIORITY_MIN = -2
const val NOTIFICATION_PRIORITY_MAX = 2

class PushSettingViewModel(): ViewModel() {
    // push options
    var enablePush = mutableStateOf(false)
    // 아래 옵션은 한국에서만 설정이 가능합니다.
    var enableAdAgreement = mutableStateOf(false)
    var enableAdAgreementNight = mutableStateOf(false)

    // notification options
    var enableForeground = mutableStateOf(false)
    var enableBadge = mutableStateOf(false)
    var notificationPriority: MutableState<Int> = mutableStateOf(0)

    val noticePriorityOptions = listOf<String>(
        "Min", "Low", "Default", "High", "Max"
    )

    fun initialFetch(activity: Activity) {
        val currentPushOptions = Gamebase.Push.queryTokenInfo(activity) { pushTokenInfo, exception ->
            if (isSuccess(exception)) {
                enablePush.value = pushTokenInfo.agreement.pushEnabled
                enableAdAgreement.value = pushTokenInfo.agreement.adAgreement
                enableAdAgreementNight.value = pushTokenInfo.agreement.adAgreementNight
            } else {
                // when fail to get push info, then initialize with false
                enablePush.value = false
                enableAdAgreement.value = false
                enableAdAgreementNight.value = false
            }
        }

        val currentNotificationOptions = Gamebase.Push.getNotificationOptions(activity)
        if (currentNotificationOptions != null) {
            enableForeground.value = currentNotificationOptions.isForegroundEnabled
            enableBadge.value = currentNotificationOptions.isBadgeEnabled
            notificationPriority.value = currentNotificationOptions.priority + NOTIFICATION_PRIORITY_MAX
        }
    }

    fun updatePushNotificationOptions(activity: Activity) {
        val pushConfiguration = PushConfiguration.newBuilder()
            .enablePush(enablePush.value)
            .enableAdAgreement(enableAdAgreement.value)
            .enableAdAgreementNight(enableAdAgreementNight.value).build()

        val notificationOptions = GamebaseNotificationOptions.newBuilder()
            .enableBadge(enableBadge.value)
            .enableForeground(enableForeground.value)
            .setPriority(notificationPriority.value + NOTIFICATION_PRIORITY_MIN).build()

//        sound file과 icon file 도 설정할 수 있습니다.
//            .setSmallIconName(smallIconName.value)
//            .setSoundFileName(soundFileName.value)

        Gamebase.Push.registerPush(activity, pushConfiguration, notificationOptions) { exception ->
            if (!isSuccess(exception)) {
                showAlert(activity, "Failed to register push", exception.printWithIndent())
            }
        }
    }
}