package com.toast.android.gamebase.sample.ui.profile

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.toast.android.gamebase.sample.GamebaseManager
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel(), DefaultLifecycleObserver {
    var userId: String by mutableStateOf("")
    var accessToken: String by mutableStateOf("")
    var lastLoggedInProvider: String by mutableStateOf("")
    var connectedIdpList: List<String> by mutableStateOf(listOf())

    override fun onStart(owner: LifecycleOwner) {
        viewModelScope.launch {
            updateData()
        }
    }

    private fun updateData() {
        userId = GamebaseManager.getUserID()
        accessToken = GamebaseManager.getAccessToken()
        lastLoggedInProvider = GamebaseManager.getLastLoggedInProvider()
        connectedIdpList = GamebaseManager.getAuthMappingList()
    }
}

@Composable
fun <lifecycleObserver : LifecycleObserver> lifecycleObserver.observeLifecycle(lifecycle: Lifecycle) {
    DisposableEffect(lifecycle) {
        lifecycle.addObserver(this@observeLifecycle)
        onDispose {
            lifecycle.removeObserver(this@observeLifecycle)
        }
    }
}