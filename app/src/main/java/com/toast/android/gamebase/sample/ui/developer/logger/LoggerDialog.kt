/*
 * © NHN Corp. All rights reserved.
 * NHN Corp. PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.toast.android.gamebase.sample.ui.developer.logger

import android.app.Activity
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
import com.toast.android.gamebase.sample.ui.components.input.DropdownMenuBox
import com.toast.android.gamebase.sample.ui.components.dialog.InputDialog
import com.toast.android.gamebase.sample.ui.components.input.TextFieldWithLabel

@Composable
fun LoggerInitializeDialog(
    activity: Activity,
    isDialogOpened: Boolean,
    title: String,
    message: String,
    setDialogStatus: (Boolean) -> Unit,
    isLoggerAppKeyValid: Boolean,
) {
    if (isDialogOpened) {
        val loggerInitializeDialogStateHolder = LoggerInitializeDialogStateHolder()
        InputDialog(
            title = title,
            labelName = stringResource(id = R.string.app_key),
            fieldMessage = message,
            setDialogStatus = setDialogStatus,
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
    setDialogStatus: (Boolean) -> Unit,
    stringArrayResources: Int
) {
    if (isDialogOpened) {
        val sendLogDialogStateHolder = SendLogDialogStateHolder()
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
                    Spacer(modifier = Modifier.height(5.dp))
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
                            sendLogDialogStateHolder.sendLogger()
                            setDialogStatus(false)
                        }
                    ) {
                        Text(stringResource(id = R.string.button_ok))
                    }
                    TextButton(
                        onClick = {
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
fun PreviewSendLogDialog() {
    SendLogDialog(
        isDialogOpened = true,
        title = "제목",
        setDialogStatus = {},
        stringArrayResources = R.array.logger_level
    )
}