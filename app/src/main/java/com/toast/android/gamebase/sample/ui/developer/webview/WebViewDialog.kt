package com.toast.android.gamebase.sample.ui.developer.webview

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.toast.android.gamebase.sample.R
import com.toast.android.gamebase.sample.gamebase_manager.openExternalBrowser
import com.toast.android.gamebase.sample.ui.components.dialog.InputDialog

@Composable
fun OpenCustomWebViewDialog(
    isDialogOpened: Boolean,
    title: String,
    fieldMessage: String = "",
    setDialogState: (Boolean) -> Unit,
    onOkButtonClicked: (String) -> Unit,
) {
    if (isDialogOpened) {
        InputDialog(
            title = title,
            labelName = stringResource(id = R.string.url),
            fieldMessage = fieldMessage,
            setDialogState = setDialogState,
            onOkButtonClicked = onOkButtonClicked
        )
    }
}

@Composable
fun OpenBrowserDialog(
    activity: Activity,
    isDialogOpened: Boolean,
    title: String,
    fieldMessage: String = "",
    setDialogState: (Boolean) -> Unit,
) {
    if (isDialogOpened) {
        InputDialog(
            title = title,
            labelName = stringResource(id = R.string.url),
            fieldMessage = fieldMessage,
            setDialogState = setDialogState,
            onOkButtonClicked = { value ->
                openExternalBrowser(activity, value)
            }
        )
    }
}

@Composable
fun WebViewSettingDialog(
    isDialogOpened: Boolean,
    title: String,
    labelName: String,
    message: String,
    setDialogState: (Boolean) -> Unit,
    onOkButtonClicked: (String) -> Unit
) {
    if (isDialogOpened) {
        InputDialog(
            title = title,
            labelName = labelName,
            fieldMessage = message,
            setDialogState = setDialogState,
            onOkButtonClicked = onOkButtonClicked
        )
    }
}