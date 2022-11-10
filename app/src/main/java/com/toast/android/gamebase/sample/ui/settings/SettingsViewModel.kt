package com.toast.android.gamebase.sample.ui.settings

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.toast.android.gamebase.base.push.PushConfiguration
import com.toast.android.gamebase.base.push.data.GamebaseNotificationOptions
import com.toast.android.gamebase.contact.ContactConfiguration
import com.toast.android.gamebase.sample.GamebaseActivity
import com.toast.android.gamebase.sample.gamebasemanager.getNotificationOptions
import com.toast.android.gamebase.sample.gamebasemanager.isSuccess
import com.toast.android.gamebase.sample.gamebasemanager.openContact
import com.toast.android.gamebase.sample.gamebasemanager.queryTokenInfo
import com.toast.android.gamebase.sample.gamebasemanager.showAlert
import com.toast.android.gamebase.sample.ui.login.LoginState
import com.toast.android.gamebase.sample.ui.navigation.SampleAppScreens
import com.toast.android.gamebase.sample.util.printWithIndent
import kotlinx.coroutines.launch
import com.toast.android.gamebase.sample.gamebasemanager.logout as gamebaseLogout
import com.toast.android.gamebase.sample.gamebasemanager.registerPush as gamebaseRegisterPush
import com.toast.android.gamebase.sample.gamebasemanager.withdraw as gamebaseWithdraw

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

    fun registerPush(activity: GamebaseActivity, pushType: PUSH_TYPE) {
        pushType.changePushState(this)

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

        foregroundState.value = !foregroundState.value
        val notificationOpitions: GamebaseNotificationOptions =
            GamebaseNotificationOptions.newBuilder().enableForeground(foregroundState.value)
                .build()

        gamebaseRegisterPush(activity, configuration, notificationOpitions) { exception ->
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
        foregroundState.value = notificationOptions.isForegroundEnabled
    }

    fun loadServiceCenter(activity: GamebaseActivity, userName: String) {
        val configuration = ContactConfiguration.newBuilder().setUserName(userName).build()
        openContact(activity, configuration) {
        }
    }

    fun navigateToIdpMapping(navController: NavController) {
        viewModelScope.launch {
            navController.navigate(SampleAppScreens.IdpMapping.route) {
                launchSingleTop = true
            }
        }
    }
}