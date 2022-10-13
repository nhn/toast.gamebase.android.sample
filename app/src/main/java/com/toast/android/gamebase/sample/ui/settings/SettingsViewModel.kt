package com.toast.android.gamebase.sample.ui.settings

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.toast.android.gamebase.sample.GamebaseActivity
import com.toast.android.gamebase.sample.GamebaseManager
import com.toast.android.gamebase.sample.ui.login.LoginState
import com.toast.android.gamebase.sample.ui.navigation.SampleAppScreens
import kotlinx.coroutines.launch

class SettingsViewModel : ViewModel() {
    var uiState by mutableStateOf(value = LoginState.LOGGED_IN)
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

}