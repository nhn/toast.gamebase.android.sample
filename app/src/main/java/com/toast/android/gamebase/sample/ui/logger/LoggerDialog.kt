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
import com.toast.android.gamebase.sample.ui.logger.LoggerInformation

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
                TextFieldWithLabel(
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
    loggerInformation: LoggerInformation,
    loggerLevelExpanded: MutableState<Boolean>,
    onOkButtonClicked: (LoggerInformation) -> Unit,
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
                    TextFieldWithLabel(
                        labelName = stringResource(id = R.string.message),
                        fieldMessage = loggerInformation.loggerMessage
                    )
                    TextFieldWithLabel(
                        labelName = stringResource(id = R.string.user_key),
                        fieldMessage = loggerInformation.loggerUserKey
                    )
                    TextFieldWithLabel(
                        labelName = stringResource(id = R.string.user_value),
                        fieldMessage = loggerInformation.loggerUserValue
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
                            selected = loggerInformation.loggerLevel.value,
                            onSelected = { selectedId ->
                                loggerInformation.loggerLevel.value = selectedId
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
                                loggerInformation
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
        loggerInformation = LoggerInformation(
            mutableStateOf(0),
            mutableStateOf("test"),
            mutableStateOf("test"),
            mutableStateOf("test")
        ),
        loggerLevelExpanded = mutableStateOf(false),
        onOkButtonClicked = { },
        onCancelButtonClicked = {},
        R.array.logger_level
    )
}