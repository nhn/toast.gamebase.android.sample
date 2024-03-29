package com.toast.android.gamebase.sample.ui.home

import android.app.Activity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.toast.android.gamebase.sample.R
import com.toast.android.gamebase.sample.gamebase_manager.addGamebaseEventHandler
import com.toast.android.gamebase.sample.gamebase_manager.showImageNotices

@Composable
fun HomeScreen(activity: Activity, onLoggedOut: () -> Unit) {
    var initialApiCalled by rememberSaveable { mutableStateOf(false) }

    Surface {
        Column (
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            if(!initialApiCalled) {
                initialApiCalled = true
                addGamebaseEventHandler(activity) {
                    onLoggedOut()
                }
                showImageNotices(activity) {}
            }
            Text(stringResource(id = R.string.home_main_text))
        }
    }
}