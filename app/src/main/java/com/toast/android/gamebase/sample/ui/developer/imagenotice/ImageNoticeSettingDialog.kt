package com.toast.android.gamebase.sample.ui.developer.imagenotice

import androidx.compose.runtime.Composable
import com.toast.android.gamebase.sample.ui.common.InputDialog

@Composable
fun ImageNoticeSettingDialog(
    isDialogOpened: Boolean,
    title: String,
    labelName: String,
    message: String,
    setDialogStatus: (Boolean) -> Unit,
    fieldEnabled: Boolean,
    onOkButtonClicked: (String) -> Unit
) {
    if (isDialogOpened) {
        InputDialog(
            title = title,
            labelName = labelName,
            fieldMessage = message,
            setDialogStatus = setDialogStatus,
            fieldEnabled = fieldEnabled,
            onOkButtonClicked = onOkButtonClicked
        )
    }
}