package com.toast.android.gamebase.sample.ui.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.toast.android.gamebase.Gamebase
import com.toast.android.gamebase.base.auth.AuthProvider
import com.toast.android.gamebase.base.auth.AuthProviderCredentialConstants
import com.toast.android.gamebase.sample.GamebaseActivity
import com.toast.android.gamebase.sample.data.lineRegionList
import com.toast.android.gamebase.sample.gamebase_manager.isLoggedIn
import com.toast.android.gamebase.sample.gamebase_manager.lastProviderLogin
import com.toast.android.gamebase.sample.gamebase_manager.loginWithIdP
import com.toast.android.gamebase.sample.gamebase_manager.registerPush
import com.toast.android.gamebase.sample.util.loadPushConfiguration

enum class LoginState {
    LOGGED_IN,
    LOGGED_OUT,
    SHOW_LINE_REGION_DIALOG,
}

class LoginViewModel : ViewModel() {
    var uiState by mutableStateOf(value = LoginState.LOGGED_OUT)
        private set
    private val enteredRegion = mutableStateOf("none")

    // 이전에 인증했던 기록이 있다면 ID와 비밀번호를 입력받지 않고 인증을 시도합니다.
    fun tryLastIdpLogin(activity: GamebaseActivity) {
        if (Gamebase.getLastLoggedInProvider() != null && !isLoggedIn()) {
            lastProviderLogin(
                activity,
                onLoginFinished = {
                    uiState = LoginState.LOGGED_IN
                },
                onLastProviderIsLine = {
                    showRegionSelectDialog()
                }
            )
        }
    }

    fun login(activity: GamebaseActivity, idp: String) {
        // you can add additionalInfo by idp
        val additionalInfo: MutableMap<String, Any> = mutableMapOf()
        if (idp == AuthProvider.LINE) {
            additionalInfo[AuthProviderCredentialConstants.LINE_CHANNEL_REGION] = enteredRegion.value
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

    fun showRegionSelectDialog() {
        uiState = LoginState.SHOW_LINE_REGION_DIALOG
    }

    fun setRegionDialogState(dialogState: Boolean) {
        if (!dialogState) {
            uiState = LoginState.LOGGED_OUT
        }
    }

    fun onRegionDialogOkButtonClicked(activity: GamebaseActivity, selected: Int) {
        enteredRegion.value = lineRegionList[selected]
        login(activity, AuthProvider.LINE)
    }
}
