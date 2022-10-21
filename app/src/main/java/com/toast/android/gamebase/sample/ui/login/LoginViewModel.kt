package com.toast.android.gamebase.sample.ui.login

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.toast.android.gamebase.sample.R
import com.toast.android.gamebase.sample.GamebaseActivity
import com.toast.android.gamebase.sample.GamebaseManager

import com.toast.android.gamebase.sample.ui.navigation.SampleAppScreens
import com.toast.android.gamebase.Gamebase
import com.toast.android.gamebase.base.auth.AuthProvider
import com.toast.android.gamebase.base.auth.AuthProviderCredentialConstants
import com.toast.android.gamebase.sample.util.loadPushConfiguration
import kotlinx.coroutines.launch

enum class LoginState() {
    LOGGED_IN,
    LOGGED_OUT
}

// Add supported Idp List
val supportedIdpList = listOf(
    AuthProvider.GUEST,
    AuthProvider.GOOGLE,
    AuthProvider.NAVER,
    AuthProvider.APPLEID,
    AuthProvider.FACEBOOK,
    AuthProvider.KAKAOGAME,
    AuthProvider.LINE,
    AuthProvider.TWITTER,
    AuthProvider.WEIBO,
    "payco",
)

private const val TAG = "LoginViewModel"

class LoginViewModel() : ViewModel() {
    var uiState by mutableStateOf(value = LoginState.LOGGED_OUT)
        private set

    fun tryLastIdpLogin(activity: GamebaseActivity) {
        if (Gamebase.getLastLoggedInProvider() != null && !GamebaseManager.isLoggedIn()) {
            GamebaseManager.lastProviderLogin(activity) {
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
        GamebaseManager.loginWithIdP(
            activity,
            idp,
            additionalInfo
        ) {
            uiState = LoginState.LOGGED_IN

            // Call registerPush with saved PushConfiguration.
            val savedPushConfiguration = loadPushConfiguration()
            if (savedPushConfiguration != null) {
                Gamebase.Push.registerPush(activity, savedPushConfiguration) {
                    Log.e(TAG, "registerPush failed! ${it.code} ${it.message}")
                };
            }
        }
    }

    fun navigateToHome(navController: NavController) {
        viewModelScope.launch {
            navController.navigate(SampleAppScreens.Home.route) {
                popUpTo(SampleAppScreens.Login.route) {
                    inclusive = true
                }
            }
        }
    }

    fun getIconResourceById(idp: String): Int {
        when(idp){
            AuthProvider.GUEST -> return R.drawable.guest_logo
            AuthProvider.GOOGLE -> return R.drawable.google_logo
            AuthProvider.NAVER -> return R.drawable.naver_logo
            AuthProvider.APPLEID -> return R.drawable.appleid_logo
            AuthProvider.FACEBOOK -> return R.drawable.facebook_logo
            AuthProvider.KAKAOGAME -> return R.drawable.kakaogames_logo
            AuthProvider.LINE -> return R.drawable.line_logo
            AuthProvider.TWITTER -> return R.drawable.twitter_logo
            AuthProvider.WEIBO -> return R.drawable.weibo_logo
            "payco" -> return R.drawable.payco_logo
        }
        return R.drawable.guest_logo
    }
}