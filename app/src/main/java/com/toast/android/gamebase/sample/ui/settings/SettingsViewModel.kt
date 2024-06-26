package com.toast.android.gamebase.sample.ui.settings

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import com.toast.android.gamebase.base.push.PushConfiguration
import com.toast.android.gamebase.base.push.data.GamebaseNotificationOptions
import com.toast.android.gamebase.contact.ContactConfiguration
import com.toast.android.gamebase.sample.GamebaseActivity
import com.toast.android.gamebase.sample.R
import com.toast.android.gamebase.sample.gamebase_manager.getNotificationOptions
import com.toast.android.gamebase.sample.gamebase_manager.isSuccess
import com.toast.android.gamebase.sample.gamebase_manager.openContact
import com.toast.android.gamebase.sample.gamebase_manager.openExternalBrowser
import com.toast.android.gamebase.sample.gamebase_manager.queryTokenInfo
import com.toast.android.gamebase.sample.gamebase_manager.showAlert
import com.toast.android.gamebase.sample.ui.login.LoginState
import com.toast.android.gamebase.sample.util.printWithIndent
import com.toast.android.gamebase.sample.gamebase_manager.logout as gamebaseLogout
import com.toast.android.gamebase.sample.gamebase_manager.registerPush as gamebaseRegisterPush
import com.toast.android.gamebase.sample.gamebase_manager.withdraw as gamebaseWithdraw

private const val TAG = "SettingsScreen"

class SettingsViewModel : ViewModel() {
    var uiState by mutableStateOf(value = LoginState.LOGGED_IN)
        private set
    var pushState = mutableStateOf(false)
        private set
    var advertisePushState = mutableStateOf(false)
        private set
    var nightAdvertisePushState = mutableStateOf(false)
        private set
    var foregroundState = mutableStateOf(false)
        private set

    fun logout(activity: GamebaseActivity) {
        gamebaseLogout(activity) { isSuccess, errorMessage ->
            if (isSuccess) {
                uiState = LoginState.LOGGED_OUT
            } else {
                val msg = errorMessage ?: ""
                showAlert(activity, "Logout Failed", msg)
            }
        }
    }

    fun withdraw(activity: GamebaseActivity) {
        gamebaseWithdraw(activity) { isSuccess, errorMessage ->
            if (isSuccess) {
                uiState = LoginState.LOGGED_OUT
            } else {
                val msg = errorMessage ?: ""
                showAlert(activity, "Withdraw Failed", msg)
            }
        }
    }

    fun requestPushStatus(activity: GamebaseActivity) {
        requestPushSettingInfo(activity)
        requestPushForegroundInfo(activity)
    }

    fun registerPush(activity: GamebaseActivity) {

        val configuration: PushConfiguration = PushConfiguration.newBuilder()
            .enablePush(pushState.value)
            .enableAdAgreement(advertisePushState.value)
            .enableAdAgreementNight(nightAdvertisePushState.value).build()

        gamebaseRegisterPush(activity, configuration) { exception ->
            if (isSuccess(exception)) {
                requestPushSettingInfo(activity)
            } else {
                showAlert(
                    activity,
                    "registerPush error",
                    exception.printWithIndent()
                )
                Log.d(
                    TAG,
                    exception.toJsonString()
                )
            }
        }
    }

    fun registerPushForeground(activity: GamebaseActivity) {
        val configuration = PushConfiguration.newBuilder()
            .enablePush(pushState.value)
            .enableAdAgreement(advertisePushState.value)
            .enableAdAgreementNight(nightAdvertisePushState.value).build()

        val notificationOptions: GamebaseNotificationOptions =
            GamebaseNotificationOptions.newBuilder().enableForeground(foregroundState.value)
                .build()

        gamebaseRegisterPush(activity, configuration, notificationOptions) { exception ->
            if (!isSuccess(exception)) {
                showAlert(
                    activity,
                    "registerPushForeground error",
                    exception.printWithIndent()
                )
                Log.d(
                    TAG,
                    exception.toJsonString()
                )
            }
        }
    }

    private fun requestPushSettingInfo(activity: GamebaseActivity) {
        queryTokenInfo(activity) { data, exception ->
            if (isSuccess(exception)) {
                pushState.value = data.agreement.pushEnabled
                advertisePushState.value = data.agreement.adAgreement
                nightAdvertisePushState.value = data.agreement.adAgreementNight
            } else {
                showAlert(
                    activity,
                    "requestPushSettingInfo error",
                    exception.printWithIndent()
                )
                Log.d(
                    TAG,
                    exception.toJsonString()
                )
            }
        }
    }

    private fun requestPushForegroundInfo(activity: GamebaseActivity) {
        val notificationOptions = getNotificationOptions(activity)
        if (notificationOptions != null) {
            foregroundState.value = notificationOptions.isForegroundEnabled
        }
    }

    fun loadServiceCenter(activity: GamebaseActivity, userName: String) {
        val configuration = ContactConfiguration.newBuilder().setUserName(userName).build()
        openContact(activity, configuration) {
        }
    }

    fun startOssLicenseMenuActivity(activity: Activity) {
        activity.startActivity(Intent(activity, OssLicensesMenuActivity::class.java))
        OssLicensesMenuActivity.setActivityTitle(
            (activity as Context).resources.getString(R.string.setting_open_source_licenses))
    }

    fun openSampleAppUrlInBrowser(activity: Activity) {
        val url = "https://github.com/nhn/toast.gamebase.android.sample"
        openExternalBrowser(activity, url)
    }
}