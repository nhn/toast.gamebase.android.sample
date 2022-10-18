package com.toast.android.gamebase.sample.ui.profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.toast.android.gamebase.sample.GamebaseManager

class ProfileViewModel : ViewModel() {
    var userId: String by mutableStateOf("")
        private set
    var accessToken: String by mutableStateOf("")
        private set
    var lastLoggedInProvider: String by mutableStateOf("")
        private set
    var connectedIdpList: List<String> by mutableStateOf(listOf())
        private set

    fun updateData() {
        userId = GamebaseManager.getUserID()
        accessToken = GamebaseManager.getAccessToken()
        lastLoggedInProvider = GamebaseManager.getLastLoggedInProvider()
        connectedIdpList = GamebaseManager.getAuthMappingList()
    }
}
