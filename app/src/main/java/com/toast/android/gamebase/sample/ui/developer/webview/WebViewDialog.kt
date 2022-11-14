package com.toast.android.gamebase.sample.ui.developer.webview

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.toast.android.gamebase.sample.R
import com.toast.android.gamebase.sample.gamebasemanager.openExternalBrowser
import com.toast.android.gamebase.sample.ui.common.InputDialog

@Composable
fun OpenCustomWebViewDialog(
    isDialogOpened: Boolean,
    title: String,
    setDialogStatus: (Boolean) -> Unit,
    fieldDisabled: Boolean,
    onOkButtonClicked: (String) -> Unit
) {
    if (isDialogOpened) {
        InputDialog(
            title = title,
            labelName = stringResource(id = R.string.ui_screen_url),
            fieldMessage = "",
            setDialogStatus = setDialogStatus,
            fieldEnabled = fieldDisabled,
            onOkButtonClicked = onOkButtonClicked
        )
    }
}

@Composable
fun OpenBrowserDialog(
    activity: Activity,
    isDialogOpened: Boolean,
    title: String,
    setDialogStatus: (Boolean) -> Unit,
    fieldDisabled: Boolean
) {
    if (isDialogOpened) {
        InputDialog(
            title = title,
            labelName = stringResource(id = R.string.ui_screen_url),
            fieldMessage = "",
            setDialogStatus = setDialogStatus,
            fieldEnabled = fieldDisabled,
            onOkButtonClicked = { value ->
                openExternalBrowser(activity, value)
            }
        )
    }
}

// TODO : IMAGE 공지 setting UI와 겹침 추후에 common.settingUI 등으로 setting 분리 요망
@Composable
fun WebViewSettingDialog(
    isDialogOpened: Boolean,
    title: String,
    labelName: String,
    message: String,
    setDialogStatus: (Boolean) -> Unit,
    fieldDisabled: Boolean,
    onOkButtonClicked: (String) -> Unit
) {
    if (isDialogOpened) {
        InputDialog(
            title = title,
            labelName = labelName,
            fieldMessage = message,
            setDialogStatus = setDialogStatus,
            fieldEnabled = fieldDisabled,
            onOkButtonClicked = onOkButtonClicked
        )
    }
}