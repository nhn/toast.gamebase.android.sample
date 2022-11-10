package com.toast.android.gamebase.sample.ui.common

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.toast.android.gamebase.sample.R

@Composable
fun LoggerInitializeDialog(
    isDialogOpened: Boolean,
    title: String,
    setDialogStatus: (Boolean) -> Unit,
    textMessage: MutableState<String>,
    onOkButtonClicked: (String) -> Unit,
    onCancelButtonClicked: () -> Unit,
    isLoggerAppKeyValid: MutableState<Boolean>
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
                TextField(
                    labelName = stringResource(id = R.string.app_key),
                    fieldMessage = textMessage,
                    fieldEnabled = isLoggerAppKeyValid
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
                            onOkButtonClicked(textMessage.value)
                            setDialogStatus(false)
                        }
                    ) {
                        Text(stringResource(id = R.string.button_ok))
                    }
                    TextButton(
                        onClick = {
                            onCancelButtonClicked()
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

@SuppressLint("UnrememberedMutableState")
@Composable
fun SendLogDialog(
    isDialogOpened: Boolean,
    title: String,
    setDialogStatus: (Boolean) -> Unit,
    loggerMessage: MutableState<String>,
    loggerUserKey: MutableState<String>,
    loggerUserValue: MutableState<String>,
    loggerLevel: MutableState<Int>,
    loggerLevelExpanded: MutableState<Boolean>,
    onOkButtonClicked: (String, String, String, Int) -> Unit,
    onCancelButtonClicked: () -> Unit,
    stringArrayResources: Int,
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
                    TextField(
                        labelName = stringResource(id = R.string.message),
                        fieldMessage = loggerMessage
                    )
                    TextField(
                        labelName = stringResource(id = R.string.user_key),
                        fieldMessage = loggerUserKey
                    )
                    TextField(
                        labelName = stringResource(id = R.string.user_value),
                        fieldMessage = loggerUserValue
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
                            expanded = loggerLevelExpanded.value,
                            onExpandChanged = { expand -> loggerLevelExpanded.value = expand },
                            selected = loggerLevel.value,
                            onSelected = { selectedId ->
                                loggerLevel.value = selectedId
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
                            onOkButtonClicked(
                                loggerMessage.value,
                                loggerUserKey.value,
                                loggerUserValue.value,
                                loggerLevel.value
                            )
                            setDialogStatus(false)
                        }
                    ) {
                        Text(stringResource(id = R.string.button_ok))
                    }
                    TextButton(
                        onClick = {
                            onCancelButtonClicked()
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


@SuppressLint("UnrememberedMutableState")
@Preview
@Composable
fun PreviewLoggerInitializeDialog() {
    LoggerInitializeDialog(
        isDialogOpened = true,
        title = "제목",
        setDialogStatus = {},
        textMessage = mutableStateOf("test"),
        onOkButtonClicked = {},
        onCancelButtonClicked = {},
        isLoggerAppKeyValid = mutableStateOf(true)
    )
}

@SuppressLint("UnrememberedMutableState")
@Preview
@Composable
fun PreviewSendLogDialog() {
    SendLogDialog(
        isDialogOpened = true,
        title = "제목",
        setDialogStatus = {},
        loggerMessage = mutableStateOf("test"),
        loggerUserKey = mutableStateOf("test"),
        loggerUserValue = mutableStateOf("test"),
        loggerLevel = mutableStateOf(0),
        loggerLevelExpanded = mutableStateOf(false),
        onOkButtonClicked = { loggerMessage, loggerUserKey, loggerUserValue, loggerLevel -> },
        onCancelButtonClicked = {},
        R.array.logger_level
    )
}