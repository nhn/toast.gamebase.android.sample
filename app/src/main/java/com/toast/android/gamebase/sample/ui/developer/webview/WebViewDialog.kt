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
    setDialogStatus: (Boolean) -> Unit,
    onOkButtonClicked: (String) -> Unit,
) {
    if (isDialogOpened) {
        InputDialog(
            title = title,
            labelName = stringResource(id = R.string.url),
            fieldMessage = fieldMessage,
            setDialogStatus = setDialogStatus,
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
    setDialogStatus: (Boolean) -> Unit,
) {
    if (isDialogOpened) {
        InputDialog(
            title = title,
            labelName = stringResource(id = R.string.url),
            fieldMessage = fieldMessage,
            setDialogStatus = setDialogStatus,
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
    setDialogStatus: (Boolean) -> Unit,
    onOkButtonClicked: (String) -> Unit
) {
    if (isDialogOpened) {
        InputDialog(
            title = title,
            labelName = labelName,
            fieldMessage = message,
            setDialogStatus = setDialogStatus,
            onOkButtonClicked = onOkButtonClicked
        )
    }
}