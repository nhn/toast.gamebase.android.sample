package com.toast.android.gamebase.sample.ui.developer.logger

import android.app.Activity
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Dialog
import com.toast.android.gamebase.sample.R
import com.toast.android.gamebase.sample.ui.components.dialog.DialogButtonRow
import com.toast.android.gamebase.sample.ui.components.input.DropdownMenuBox
import com.toast.android.gamebase.sample.ui.components.dialog.InputDialog
import com.toast.android.gamebase.sample.ui.components.input.TextFieldWithLabel

@Composable
fun LoggerInitializeDialog(
    activity: Activity,
    isDialogOpened: Boolean,
    title: String,
    message: String,
    setDialogState: (Boolean) -> Unit,
    isLoggerAppKeyValid: Boolean,
) {
    if (isDialogOpened) {
        val loggerInitializeDialogStateHolder = LoggerInitializeDialogStateHolder()
        InputDialog(
            title = title,
            labelName = stringResource(id = R.string.app_key),
            fieldMessage = message,
            setDialogState = setDialogState,
            fieldEnabled = !isLoggerAppKeyValid,
            onOkButtonClicked = { value ->
                loggerInitializeDialogStateHolder.initializeLogger(
                    activity = activity,
                    appKey = value
                )
            }
        )
    }
}

@Composable
fun SendLogDialog(
    isDialogOpened: Boolean,
    title: String,
    setDialogState: (Boolean) -> Unit,
    stringArrayResources: Int
) {
    if (isDialogOpened) {
        val sendLogDialogStateHolder = SendLogDialogStateHolder()

        Dialog(onDismissRequest = {}) {
            val scrollState = rememberScrollState()
            Surface(
                modifier = Modifier
                    .clip(RoundedCornerShape(dimensionResource(id = R.dimen.logger_dialog_surface_corner_shape)))
            ) {
                Column(
                    modifier = Modifier
                        .padding(
                            dimensionResource(id = R.dimen.logger_dialog_surface_padding)
                        )
                        .fillMaxWidth()
                        .verticalScroll(scrollState)
                ) {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(dimensionResource(id = R.dimen.logger_dialog_text_padding)),
                        text = title,
                        textAlign = TextAlign.Center
                    )
                    Column() {
                        TextFieldWithLabel(
                            labelName = stringResource(id = R.string.message),
                            fieldMessage = sendLogDialogStateHolder.loggerMessage.value,
                            onValueChanged = { value ->
                                sendLogDialogStateHolder.loggerMessage.value = value
                            }
                        )
                        TextFieldWithLabel(
                            labelName = stringResource(id = R.string.user_key),
                            fieldMessage = sendLogDialogStateHolder.loggerUserKey.value,
                            onValueChanged = { value ->
                                sendLogDialogStateHolder.loggerUserKey.value = value
                            }
                        )
                        TextFieldWithLabel(
                            labelName = stringResource(id = R.string.user_value),
                            fieldMessage = sendLogDialogStateHolder.loggerUserValue.value,
                            onValueChanged = { value ->
                                sendLogDialogStateHolder.loggerUserValue.value = value
                            }
                        )
                        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.logger_dialog_spacer_padding)))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = stringResource(id = R.string.developer_push_noti_enable_set_priority))
                            DropdownMenuBox(
                                options = stringArrayResource(id = stringArrayResources).toList(),
                                expanded = sendLogDialogStateHolder.loggerLevelExpanded.value,
                                onExpandChanged = { expand ->
                                    sendLogDialogStateHolder.loggerLevelExpanded.value = expand
                                },
                                selected = sendLogDialogStateHolder.loggerLevel.value,
                                onSelected = { selectedId ->
                                    sendLogDialogStateHolder.loggerLevel.value = selectedId
                                },
                                modifier = Modifier.width(dimensionResource(id = R.dimen.logger_dialog_drop_down_box_width))
                            )
                        }
                    }
                    DialogButtonRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = dimensionResource(id = R.dimen.logger_dialog_text_padding)),
                        onOkButtonClicked = {
                            sendLogDialogStateHolder.sendLogger()
                            setDialogState(false)
                        },
                        onCancelButtonClicked = {
                            setDialogState(false)
                        },
                    )
                }
            }
        }
    }
}

@Composable
fun SendReportDialog(
    isDialogOpened: Boolean,
    title: String,
    setDialogState: (Boolean) -> Unit
) {
    if (isDialogOpened) {
        val sendReportDialogStateHolder = SendReportDialogStateHolder()

        Dialog(onDismissRequest = {}) {
            val scrollState = rememberScrollState()
            Surface(
                modifier = Modifier
                    .clip(RoundedCornerShape(dimensionResource(id = R.dimen.logger_dialog_surface_corner_shape)))
            ) {
                Column(
                    modifier = Modifier
                        .padding(
                            dimensionResource(id = R.dimen.logger_dialog_surface_padding)
                        )
                        .fillMaxWidth()
                        .verticalScroll(scrollState)
                ) {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(dimensionResource(id = R.dimen.logger_dialog_text_padding)),
                        text = title,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        modifier = Modifier
                            .fillMaxWidth(),
                        text = stringResource(id = R.string.send_report_description),
                        textAlign = TextAlign.Left
                    )
                    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.logger_dialog_spacer_padding)))
                    Column() {
                        TextFieldWithLabel(
                            labelName = stringResource(id = R.string.message),
                            fieldMessage = sendReportDialogStateHolder.loggerMessage.value,
                            onValueChanged = { value ->
                                sendReportDialogStateHolder.loggerMessage.value = value
                            }
                        )
                        TextFieldWithLabel(
                            labelName = stringResource(id = R.string.user_key),
                            fieldMessage = sendReportDialogStateHolder.loggerUserKey.value,
                            onValueChanged = { value ->
                                sendReportDialogStateHolder.loggerUserKey.value = value
                            }
                        )
                        TextFieldWithLabel(
                            labelName = stringResource(id = R.string.user_value),
                            fieldMessage = sendReportDialogStateHolder.loggerUserValue.value,
                            onValueChanged = { value ->
                                sendReportDialogStateHolder.loggerUserValue.value = value
                            }
                        )
                        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.logger_dialog_spacer_padding)))
                    }
                    DialogButtonRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = dimensionResource(id = R.dimen.logger_dialog_text_padding)),
                        onOkButtonClicked = {
                            sendReportDialogStateHolder.sendReport()
                            setDialogState(false)
                        },
                        onCancelButtonClicked = {
                            setDialogState(false)
                        },
                    )
                }
            }
        }
    }
}


@Preview
@Composable
fun PreviewSendLogDialogInPortrait() {
    SendLogDialog(
        isDialogOpened = true,
        title = "제목",
        setDialogState = {},
        stringArrayResources = R.array.logger_level
    )
}

@Preview
@Composable
fun PreviewSendReportDialogInPortrait() {
    SendReportDialog(
        isDialogOpened = true,
        title = "제목",
        setDialogState = {}
    )
}

@Preview(device = Devices.AUTOMOTIVE_1024p)
@Composable
fun PreviewSendLogDialogInLandscape() {
    SendLogDialog(
        isDialogOpened = true,
        title = "제목",
        setDialogState = {},
        stringArrayResources = R.array.logger_level
    )
}

@Preview(device = Devices.AUTOMOTIVE_1024p)
@Composable
fun PreviewSendReportDialogInLandscape() {
    SendReportDialog(
        isDialogOpened = true,
        title = "제목",
        setDialogState = {}
    )
}