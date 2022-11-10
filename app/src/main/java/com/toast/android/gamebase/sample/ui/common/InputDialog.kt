package com.toast.android.gamebase.sample.ui.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.toast.android.gamebase.sample.R
import com.toast.android.gamebase.sample.ui.theme.*

@Composable
fun InputDialog(
    isDialogOpened: Boolean,
    setDialogStatus: (Boolean) -> Unit,
    inputTemplate: String = "",
    onOkButtonClicked: (inputText: String) -> Unit,
) {
    var inputText by remember { mutableStateOf(inputTemplate) }

    if (isDialogOpened) {
        Dialog(onDismissRequest = {  }) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth(80f)
                    .wrapContentHeight(),
                shape = RoundedCornerShape(12.dp),
                color = White
            ) {
                Column (modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center) {
                    OutlinedTextField(
                        modifier = Modifier.width(250.dp),
                        value = inputText,
                        onValueChange = { inputText = it },
                        label = { Text("URL") }
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly) {
                        TextButton(
                            onClick = {
                                onOkButtonClicked(inputText)
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
            }
        }
    }
}

@Composable
fun InputDialog(
    title: String,
    fieldMessage: String,
    setDialogStatus: (Boolean) -> Unit,
    fieldEnabled: Boolean,
    onOkButtonClicked: (String) -> Unit
) {
    var inputText by remember { mutableStateOf(fieldMessage) }

    AlertDialog(onDismissRequest = { setDialogStatus(false) },
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
                fieldMessage = inputText,
                fieldEnabled = fieldEnabled,
                onValueChanged = { value ->
                    inputText = value
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
                        onOkButtonClicked(inputText)
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
