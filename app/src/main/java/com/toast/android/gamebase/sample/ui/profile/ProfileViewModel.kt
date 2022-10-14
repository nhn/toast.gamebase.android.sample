package com.toast.android.gamebase.sample.ui.profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.toast.android.gamebase.Gamebase

class ProfileViewModel : ViewModel() {
    var userId: String by mutableStateOf("")
    var accessToken: String by mutableStateOf("")
    var lastLoggedInProvider: String by mutableStateOf("")
    var connectedIdpList: List<String> by mutableStateOf(listOf())

    fun updateData() {
        userId = Gamebase.getUserID()
        accessToken = Gamebase.getAccessToken()
        lastLoggedInProvider = Gamebase.getLastLoggedInProvider() ?: ""
        connectedIdpList = Gamebase.getAuthMappingList()
    }
}