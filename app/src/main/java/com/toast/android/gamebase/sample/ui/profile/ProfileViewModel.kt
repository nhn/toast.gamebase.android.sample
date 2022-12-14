/*
 * © NHN Corp. All rights reserved.
 * NHN Corp. PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.toast.android.gamebase.sample.ui.profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.toast.android.gamebase.sample.gamebase_manager.*

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
        getUserID()?.let {
            userId = it
        }
        getAccessToken()?.let {
            accessToken = it
        }
        getLastLoggedInProvider()?.let {
            lastLoggedInProvider = it
        }
        connectedIdpList = getAuthMappingList()
    }
}
