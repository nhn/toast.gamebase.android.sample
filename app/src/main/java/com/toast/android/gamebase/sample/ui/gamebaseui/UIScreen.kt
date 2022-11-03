package com.toast.android.gamebase.sample.ui.gamebaseui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.toast.android.gamebase.sample.GamebaseActivity
import com.toast.android.gamebase.sample.R
import com.toast.android.gamebase.sample.gamebasemanager.showImageNotices
import com.toast.android.gamebase.sample.gamebasemanager.showWebView
import com.toast.android.gamebase.sample.ui.common.InputDialog
import com.toast.android.gamebase.sample.ui.common.RoundButton

@Composable
fun UIScreen() {
    val activity = LocalContext.current as GamebaseActivity
    var isDialogOpened by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()
    Column(
        Modifier
            .verticalScroll(scrollState)
            .padding(30.dp),)
    {
        InputDialog(
            isDialogOpened,
            { newState -> isDialogOpened = newState},
            "http://"
        ) { inputText ->
            showWebView(activity, inputText)
        }
        RoundButton(stringResource(id = R.string.ui_screen_image_notice)) {
            showImageNotices(activity) {}
        }
        RoundButton(stringResource(id = R.string.ui_screen_webview)) {
            isDialogOpened = true
        }
    }
}
