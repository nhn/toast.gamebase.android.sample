package com.toast.android.gamebase.sample.ui.profile

import android.app.Activity
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.toast.android.gamebase.sample.gamebase_manager.*

class ProfileViewModel : ViewModel() {
    var userId: String by mutableStateOf(getUserID() ?: "")
        private set
    var accessToken: String by mutableStateOf(getAccessToken() ?: "")
        private set
    var pushToken: String by mutableStateOf("")
        private set
    var lastLoggedInProvider: String by mutableStateOf(getLastLoggedInProvider()?: "")
        private set
    var connectedIdpList: List<String> by mutableStateOf(getAuthMappingList())
        private set

    fun updatePushToken(activity: Activity) {
        queryTokenInfo(activity) {
            pushTokenInfo, exception ->
            if (isSuccess(exception)) {
                pushToken = pushTokenInfo.token
            }
        }
    }
}
