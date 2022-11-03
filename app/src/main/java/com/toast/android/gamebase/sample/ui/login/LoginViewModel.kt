package com.toast.android.gamebase.sample.ui.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.toast.android.gamebase.Gamebase
import com.toast.android.gamebase.base.auth.AuthProvider
import com.toast.android.gamebase.base.auth.AuthProviderCredentialConstants
import com.toast.android.gamebase.sample.GamebaseActivity
import com.toast.android.gamebase.sample.gamebasemanager.isLoggedIn
import com.toast.android.gamebase.sample.gamebasemanager.lastProviderLogin
import com.toast.android.gamebase.sample.gamebasemanager.loginWithIdP
import com.toast.android.gamebase.sample.gamebasemanager.registerPush
import com.toast.android.gamebase.sample.ui.navigation.SampleAppScreens
import com.toast.android.gamebase.sample.util.loadPushConfiguration
import kotlinx.coroutines.launch

enum class LoginState() {
    LOGGED_IN,
    LOGGED_OUT
}

private const val TAG = "LoginViewModel"

class LoginViewModel() : ViewModel() {
    var uiState by mutableStateOf(value = LoginState.LOGGED_OUT)
        private set

    fun tryLastIdpLogin(activity: GamebaseActivity) {
        if (Gamebase.getLastLoggedInProvider() != null && !isLoggedIn()) {
            lastProviderLogin(activity) {
                uiState = LoginState.LOGGED_IN
            }
        }
    }

    fun login(activity: GamebaseActivity, idp: String) {
        // you can add additionalInfo by idp
        val additionalInfo: MutableMap<String, Any> = mutableMapOf()
        if (idp == AuthProvider.LINE) {
            // TODO: Add real current region for LINE login
            additionalInfo[AuthProviderCredentialConstants.LINE_CHANNEL_REGION] = "japan"
        }
        loginWithIdP(
            activity,
            idp,
            additionalInfo
        ) {
            uiState = LoginState.LOGGED_IN

            // Call registerPush with saved PushConfiguration.
            val savedPushConfiguration = loadPushConfiguration()
            savedPushConfiguration?.let {
                registerPush(activity, savedPushConfiguration, callback = null)
            }
        }
    }
}