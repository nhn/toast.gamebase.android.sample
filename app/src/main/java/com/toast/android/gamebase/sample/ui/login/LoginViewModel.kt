package com.toast.android.gamebase.sample.ui.login

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
import kotlinx.coroutines.launch

enum class LoginState() {
    LOGGED_IN,
    LOGGED_OUT
}

// Add supported Idp List
val supportedIdpList = listOf(
    "guest",
    "google",
    "naver",
    "appleid",
    "facebook",
    "kakaogame",
    "line",
    "twitter",
    "weibo",
    "payco",
)

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
        val additionalInfo: Map<String, Any> = HashMap()
        GamebaseManager.loginWithIdP(
            activity,
            idp,
            additionalInfo
        ) {
            uiState = LoginState.LOGGED_IN
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
            "guest" -> return R.drawable.guest_logo
            "google" -> return R.drawable.google_logo
            "naver" -> return R.drawable.naver_logo
            "appleid" -> return R.drawable.appleid_logo
            "facebook" -> return R.drawable.facebook_logo
            "kakaogame" -> return R.drawable.kakaogames_logo
            "line" -> return R.drawable.line_logo
            "twitter" -> return R.drawable.twitter_logo
            "weibo" -> return R.drawable.weibo_logo
            "payco" -> return R.drawable.payco_logo
        }
        return R.drawable.guest_logo
    }
}