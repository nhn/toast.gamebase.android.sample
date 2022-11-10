package com.toast.android.gamebase.sample.ui.common

import android.app.Activity
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.toast.android.gamebase.sample.R
import com.toast.android.gamebase.sample.ui.logger.LoggerInitializeDialogState
import com.toast.android.gamebase.sample.ui.logger.SendLogDialogState

@Composable
fun LoggerInitializeDialog(
    activity: Activity,
    isDialogOpened: Boolean,
    title: String,
    setDialogStatus: (Boolean) -> Unit,
    loggerInitializeDialogState: LoggerInitializeDialogState,
    isLoggerAppKeyValid: Boolean,
) {
    if (isDialogOpened) {
        AlertDialog(onDismissRequest = {
            setDialogStatus(false)
        },
            title = {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 50.dp),
                    text = title,
                    textAlign = TextAlign.Center
                )
            },
            text = {
                TextFieldWithLabel(
                    labelName = stringResource(id = R.string.app_key),
                    fieldMessage = loggerInitializeDialogState.loggerAppKey.value,
                    fieldEnabled = isLoggerAppKeyValid,
                    onValueChanged = { value ->
                        loggerInitializeDialogState.loggerAppKey.value = value
                    }
                )
            },
            buttons = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    TextButton(
                        onClick = {
                            loggerInitializeDialogState.initializeLogger(
                                activity = activity,
                                loggerInitializeDialogState.loggerAppKey.value
                            )
                            loggerInitializeDialogState.refreshAppKey()
                            setDialogStatus(false)
                        }
                    ) {
                        Text(stringResource(id = R.string.button_ok))
                    }
                    TextButton(
                        onClick = {
                            loggerInitializeDialogState.refreshAppKey()
                            setDialogStatus(false)
                        }
                    ) {
                        Text(stringResource(id = R.string.button_cancel))
                    }

                }
            }
        )
    }
}

@Composable
fun SendLogDialog(
    isDialogOpened: Boolean,
    title: String,
    setDialogStatus: (Boolean) -> Unit,
    sendLogDialogState: SendLogDialogState,
    stringArrayResources: Int
) {
    if (isDialogOpened) {
        AlertDialog(modifier = Modifier.fillMaxWidth(), onDismissRequest = {
            setDialogStatus(false)
        },
            title = {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 50.dp),
                    text = title,
                    textAlign = TextAlign.Center
                )
            },
            text = {
                Column(modifier = Modifier.fillMaxWidth()) {
                    TextFieldWithLabel(
                        labelName = stringResource(id = R.string.message),
                        fieldMessage = sendLogDialogState.loggerMessage.value,
                        onValueChanged = { value ->
                            sendLogDialogState.loggerMessage.value = value
                        }
                    )
                    TextFieldWithLabel(
                        labelName = stringResource(id = R.string.user_key),
                        fieldMessage = sendLogDialogState.loggerUserKey.value,
                        onValueChanged = { value ->
                            sendLogDialogState.loggerUserKey.value = value
                        }
                    )
                    TextFieldWithLabel(
                        labelName = stringResource(id = R.string.user_value),
                        fieldMessage = sendLogDialogState.loggerUserValue.value,
                        onValueChanged = { value ->
                            sendLogDialogState.loggerUserValue.value = value
                        }
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = stringResource(id = R.string.developer_push_noti_enable_set_priority))
                        DropdownMenuBox(
                            options = stringArrayResource(id = stringArrayResources).toList(),
                            expanded = sendLogDialogState.loggerLevelExpanded.value,
                            onExpandChanged = { expand ->
                                sendLogDialogState.loggerLevelExpanded.value = expand
                            },
                            selected = sendLogDialogState.loggerLevel.value,
                            onSelected = { selectedId ->
                                sendLogDialogState.loggerLevel.value = selectedId
                            },
                            modifier = Modifier.width(150.dp)
                        )
                    }
                }
            },
            buttons = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    TextButton(
                        onClick = {
                            sendLogDialogState.sendLogger()
                            sendLogDialogState.refreshLoggerInformation()
                            setDialogStatus(false)
                        }
                    ) {
                        Text(stringResource(id = R.string.button_ok))
                    }
                    TextButton(
                        onClick = {
                            sendLogDialogState.refreshLoggerInformation()
                            setDialogStatus(false)
                        }
                    ) {
                        Text(stringResource(id = R.string.button_cancel))
                    }
                }
            }
        )
    }
}

@Preview
@Composable
fun PreviewLoggerInitializeDialog() {
    var activity = LocalContext.current as Activity
    LoggerInitializeDialog(
        activity = activity,
        isDialogOpened = true,
        title = "제목",
        setDialogStatus = {},
        loggerInitializeDialogState = LoggerInitializeDialogState(),
        isLoggerAppKeyValid = true
    )
}

@Preview
@Composable
fun PreviewSendLogDialog() {
    SendLogDialog(
        isDialogOpened = true,
        title = "제목",
        setDialogStatus = {},
        sendLogDialogState = SendLogDialogState(),
        stringArrayResources = R.array.logger_level
    )
}