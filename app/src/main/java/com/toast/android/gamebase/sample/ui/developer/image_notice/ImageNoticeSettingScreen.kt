package com.toast.android.gamebase.sample.ui.developer.image_notice

import android.app.Activity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.toast.android.gamebase.sample.R
import com.toast.android.gamebase.sample.ui.components.text.ClickableText
import com.toast.android.gamebase.sample.ui.components.dialog.InputDialog
import com.toast.android.gamebase.sample.ui.components.button.RoundButton
import com.toast.android.gamebase.sample.ui.components.input.SwitchWithLabel

@Composable
fun ImageNoticeSettingScreen(
    activity: Activity = LocalContext.current as Activity,
    viewModel: ImageNoticeSettingViewModel = viewModel()
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp)
            .verticalScroll(scrollState)
    ) {
        ClickableText(stringId = R.string.developer_image_notice_configuration_background_color) {
            viewModel.imageNoticeBackgroundDialogState.value = true
        }
        ClickableText(stringId = R.string.developer_image_notice_configuration_time_out) {
            viewModel.imageNoticeTimeOutDialogState.value = true
        }
        SwitchWithLabel(
            label = stringResource(id = R.string.developer_image_notice_configuration_auto_close_custom_scheme),
            state = viewModel.autoCloseCustomSchemeSwitchState.value,
            enableSwitch = true,
            event = { newState ->
                viewModel.autoCloseCustomSchemeSwitchState.value = newState
            })
        RoundButton(buttonText = stringResource(id = R.string.developer_image_notice_show)) {
            viewModel.showUserSettingImageNotice(activity)
        }
    }
    if (viewModel.imageNoticeBackgroundDialogState.value) {
        InputDialog(
            title = stringResource(id = R.string.developer_image_notice_configuration_background_color),
            labelName = stringResource(id = R.string.developer_image_notice_configuration_background_color_label_name),
            fieldMessage = viewModel.imageNoticeBackgroundColor.value,
            setDialogState = { newState ->
                viewModel.imageNoticeBackgroundDialogState.value = newState
            },
            onOkButtonClicked = { value ->
                viewModel.imageNoticeBackgroundColor.value = value
            }
        )
    }
    if (viewModel.imageNoticeTimeOutDialogState.value) {
        InputDialog(
            title = stringResource(id = R.string.developer_image_notice_configuration_time_out),
            labelName = stringResource(id = R.string.developer_image_notice_configuration_time_out_label_name),
            fieldMessage = viewModel.imageNoticeTimeOut.value.toString(),
            setDialogState = { newState ->
                viewModel.imageNoticeTimeOutDialogState.value = newState
            },
            onOkButtonClicked = { value ->
                viewModel.imageNoticeTimeOut.value = value.toLong()
            }
        )
    }
}