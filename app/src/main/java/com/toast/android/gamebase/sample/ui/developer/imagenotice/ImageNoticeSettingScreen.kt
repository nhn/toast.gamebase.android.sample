package com.toast.android.gamebase.sample.ui.developer.imagenotice

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
import com.toast.android.gamebase.sample.ui.common.ClickableText
import com.toast.android.gamebase.sample.ui.common.InputDialog
import com.toast.android.gamebase.sample.ui.common.RoundButton
import com.toast.android.gamebase.sample.ui.common.SwitchWithLabel

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
            viewModel.imageNoticeBackgroundDialogStatue.value = true
        }
        ClickableText(stringId = R.string.developer_image_notice_configuration_time_out) {
            viewModel.imageNoticeTimeOutDialogStatus.value = true
        }
        SwitchWithLabel(
            label = stringResource(id = R.string.developer_image_notice_configuration_auto_close_custom_scheme),
            state = viewModel.autoCloseCustomSchemeSwitchStatue.value,
            enableSwitch = true,
            event = { newState ->
                viewModel.autoCloseCustomSchemeSwitchStatue.value = newState
            })
        RoundButton(buttonText = stringResource(id = R.string.developer_image_notice_show)) {
            viewModel.showUserSettingImageNotice(activity)
        }
    }
    if (viewModel.imageNoticeBackgroundDialogStatue.value) {
        InputDialog(
            title = stringResource(id = R.string.developer_image_notice_configuration_background_color),
            labelName = stringResource(id = R.string.developer_image_notice_configuration_background_color_label_name),
            fieldMessage = viewModel.imageNoticeBackgroundColor.value,
            setDialogStatus = { newState ->
                viewModel.imageNoticeBackgroundDialogStatue.value = newState
            },
            onOkButtonClicked = { value ->
                viewModel.imageNoticeBackgroundColor.value = value
            }
        )
    }
    if (viewModel.imageNoticeTimeOutDialogStatus.value) {
        InputDialog(
            title = stringResource(id = R.string.developer_image_notice_configuration_time_out),
            labelName = stringResource(id = R.string.developer_image_notice_configuration_time_out_label_name),
            fieldMessage = viewModel.imageNoticeTimeOut.value.toString(),
            setDialogStatus = { newState ->
                viewModel.imageNoticeTimeOutDialogStatus.value = newState
            },
            onOkButtonClicked = { value ->
                viewModel.imageNoticeTimeOut.value = value.toLong()
            }
        )
    }
}