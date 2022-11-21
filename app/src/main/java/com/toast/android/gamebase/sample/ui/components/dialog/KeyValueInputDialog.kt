package com.toast.android.gamebase.sample.ui.components.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Dialog
import com.toast.android.gamebase.sample.R
import com.toast.android.gamebase.sample.gamebasemanager.showAlert
import com.toast.android.gamebase.sample.ui.theme.White

@Composable
fun KeyValueInputDialog(
    title: String,
    isDialogOpened: Boolean,
    setDialogStatus: (Boolean) -> Unit,
    inputLabelForKey: String = "key",
    inputLabelForValue: String = "value",
    onOkButtonClicked: (inputKey: String, inputValue: String) -> Unit,
) {
    if (isDialogOpened) {
        var inputKey by remember { mutableStateOf("") }
        var inputValue by remember { mutableStateOf("") }

        val scrollState = rememberScrollState()
        Dialog(onDismissRequest = {  }) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth(80f)
                    .wrapContentHeight(),
                shape = RoundedCornerShape(
                    dimensionResource(id = R.dimen.key_value_input_dialog_round_corner_radius)
                ),
                color = White
            ) {
                Column (
                    modifier = Modifier.padding(
                        dimensionResource(id = R.dimen.key_value_input_dialog_column_padding)
                    ),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.h5,
                        modifier = Modifier.align(Alignment.Start)
                    )
                    Column(modifier = Modifier.verticalScroll(scrollState)) {
                        Spacer(modifier = Modifier.height(
                            dimensionResource(id = R.dimen.key_value_input_dialog_column_padding)
                        ))
                        if (inputKey.isEmpty()) {
                            Text(text = stringResource(R.string.enter_key_message),
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Start,
                                color = Red,
                                style = MaterialTheme.typography.caption)
                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly) {
                            OutlinedTextField(
                                modifier = Modifier.width(
                                    dimensionResource(id = R.dimen.key_value_input_dialog_key_text_field_width)
                                ),
                                value = inputKey,
                                onValueChange = { inputKey = it },
                                label = { Text(inputLabelForKey) },
                                singleLine = true,
                                isError = inputKey.isEmpty()
                            )
                            OutlinedTextField(
                                modifier = Modifier.width(
                                    dimensionResource(id = R.dimen.key_value_input_dialog_value_text_field_width)
                                ),
                                value = inputValue,
                                onValueChange = { inputValue = it },
                                label = { Text(inputLabelForValue) },
                                singleLine = true
                            )
                        }
                        Spacer(modifier = Modifier.height(
                            dimensionResource(id = R.dimen.key_value_input_dialog_column_padding)
                        ))
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly) {
                        TextButton(
                            onClick = {
                                if (inputKey.isEmpty()) {
                                    return@TextButton
                                }
                                onOkButtonClicked(inputKey, inputValue)
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

@Preview
@Composable
fun PreviewKeyValueInputDialog() {
    KeyValueInputDialog(
        title = "Title",
        isDialogOpened = true,
        setDialogStatus = {},
        onOkButtonClicked = { _, _ ->})
}
