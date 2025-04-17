package com.toast.android.gamebase.sample.ui.developer.game_notice

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
import com.toast.android.gamebase.sample.ui.components.button.RoundButton
import com.toast.android.gamebase.sample.ui.components.dialog.InputDialog
import com.toast.android.gamebase.sample.ui.components.text.ClickableText

@Composable
fun GameNoticeSettingScreen(
    activity: Activity = LocalContext.current as Activity,
    viewModel: GameNoticeSettingViewModel = viewModel()
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp)
            .verticalScroll(scrollState)
    ) {
        ClickableText(stringId = R.string.developer_game_notice_configuration_background_color) {
            viewModel.gameNoticeBackgroundDialogState.value = true
        }
        RoundButton(buttonText = stringResource(id = R.string.developer_game_notice_open)) {
            viewModel.openUserSettingGameNotice(activity)
        }
    }
    if (viewModel.gameNoticeBackgroundDialogState.value) {
        InputDialog(
            title = stringResource(id = R.string.developer_image_notice_configuration_background_color),
            labelName = stringResource(id = R.string.developer_image_notice_configuration_background_color_label_name),
            fieldMessage = viewModel.gameNoticeBackgroundColor.value,
            setDialogState = { newState ->
                viewModel.gameNoticeBackgroundDialogState.value = newState
            },
            onOkButtonClicked = { value ->
                viewModel.gameNoticeBackgroundColor.value = value
            }
        )
    }
}