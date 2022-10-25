package com.toast.android.gamebase.sample.ui.settings

import android.util.Log
import androidx.compose.runtime.*
import androidx.lifecycle.*
import androidx.navigation.NavController
import com.toast.android.gamebase.base.push.PushConfiguration
import com.toast.android.gamebase.base.push.data.GamebaseNotificationOptions
import com.toast.android.gamebase.sample.GamebaseActivity
import com.toast.android.gamebase.sample.GamebaseManager
import com.toast.android.gamebase.sample.ui.login.LoginState
import com.toast.android.gamebase.sample.ui.navigation.SampleAppScreens
import kotlinx.coroutines.launch

private const val TAG = "SettingsScreen"

enum class PUSH_TYPE() {
    NORMAL_PUSH {
        override fun changePushState(settingsViewModel: SettingsViewModel) {
            settingsViewModel.pushState.value = !settingsViewModel.pushState.value
        }
    },
    ADVERTISING_PUSH {
        override fun changePushState(settingsViewModel: SettingsViewModel) {
            settingsViewModel.advertisePushState.value = !settingsViewModel.advertisePushState.value
        }
    },
    NIGHT_ADVERTISING_PUSH {
        override fun changePushState(settingsViewModel: SettingsViewModel) {
            settingsViewModel.nightAdvertisePushState.value =
                !settingsViewModel.nightAdvertisePushState.value
        }
    };

    abstract fun changePushState(settingsViewModel: SettingsViewModel)
}

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

    fun navigateToLogin(navController: NavController) {
        viewModelScope.launch {
            navController.navigate(SampleAppScreens.Login.route) {
                popUpTo(SampleAppScreens.Home.route) {
                    inclusive = true
                }
                launchSingleTop = true
            }
        }
    }

    fun logout(activity: GamebaseActivity) {
        GamebaseManager.logout(activity) { isSuccess, errorMessage ->
            if (isSuccess) {
                uiState = LoginState.LOGGED_OUT
            } else {
                val msg = errorMessage ?: ""
                GamebaseManager.showError(activity, "Logout Failed", msg)
            }
        }
    }

    fun withdraw(activity: GamebaseActivity) {
        GamebaseManager.withdraw(activity) { isSuccess, errorMessage ->
            if (isSuccess) {
                uiState = LoginState.LOGGED_OUT
            } else {
                val msg = errorMessage ?: ""
                GamebaseManager.showError(activity, "Withdraw Failed", msg)
            }
        }
    }

    fun requestPushStatus(activity: GamebaseActivity) {
        requestPushSettingInfo(activity)
        requestPushForegroundInfo(activity)
    }

    fun registerPush(activity: GamebaseActivity, pushType: PUSH_TYPE) {
        pushType.changePushState(this)

        val configuration: PushConfiguration = PushConfiguration.newBuilder()
            .enablePush(pushState.value)
            .enableAdAgreement(advertisePushState.value)
            .enableAdAgreementNight(nightAdvertisePushState.value).build()

        GamebaseManager.registerPush(activity, configuration) { exception ->
            if (GamebaseManager.isSuccess(exception)) {
                requestPushSettingInfo(activity)
            } else {
                GamebaseManager.showAlert(
                    activity,
                    "registerPush error",
                    exception.toJsonString()
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

        foregroundState.value = !foregroundState.value
        val notificationOpitions: GamebaseNotificationOptions =
            GamebaseNotificationOptions.newBuilder().enableForeground(foregroundState.value)
                .build()

        GamebaseManager.registerPush(activity, configuration, notificationOpitions) { exception ->
            if (!GamebaseManager.isSuccess(exception)) {
                GamebaseManager.showAlert(
                    activity,
                    "registerPushForeground error",
                    exception.toJsonString()
                )
                Log.d(
                    TAG,
                    exception.toJsonString()
                )
            }
        }
    }

    private fun requestPushSettingInfo(activity: GamebaseActivity) {
        GamebaseManager.queryTokenInfo(activity) { data, exception ->
            if (GamebaseManager.isSuccess(exception)) {
                pushState.value = data.agreement.pushEnabled
                advertisePushState.value = data.agreement.adAgreement
                nightAdvertisePushState.value = data.agreement.adAgreementNight
            } else {
                GamebaseManager.showAlert(
                    activity,
                    "requestPushSettingInfo error",
                    exception.toJsonString()
                )
                Log.d(
                    TAG,
                    exception.toJsonString()
                )
            }
        }
    }

    private fun requestPushForegroundInfo(activity: GamebaseActivity) {
        val notificationOptions = GamebaseManager.getNotificationOptions(activity)
        foregroundState.value = notificationOptions.isForegroundEnabled
    }

    fun loadServiceCenter(activity: GamebaseActivity, userName: String?) {
        GamebaseManager.openContact(activity, userName) {
        }
    }
}