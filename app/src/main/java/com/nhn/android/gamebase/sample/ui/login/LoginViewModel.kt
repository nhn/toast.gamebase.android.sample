package com.nhn.android.gamebase.sample.ui.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nhn.android.gamebase.sample.ui.navigation.SampleAppScreens
import kotlinx.coroutines.launch

class LoginViewModel() : ViewModel() {
    val supportedIdpList = listOf(
        "guest",
        "google",
        "naver",
        "applieid",
        "facebook",
        "kakaogame",
        "line",
        "twitter",
        "weibo",
        "payco",
    )

    fun login(idp: String, onLoginSuccess: () -> Unit) {

        // TODO: gamebase login

        viewModelScope.launch {
            onLoginSuccess()
        }
    }
}