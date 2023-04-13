package com.toast.android.gamebase.sample.ui.components.input

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.toast.android.gamebase.sample.ui.theme.Black
import com.toast.android.gamebase.sample.ui.theme.LightBlue
import com.toast.android.gamebase.sample.ui.theme.Toast
import com.toast.android.gamebase.sample.ui.theme.Transparent

@Composable
fun TextFieldWithLabel(
    labelName: String,
    fieldMessage: String,
    fieldEnabled: Boolean = true,
    showClearButton: Boolean = false,
    onValueChanged: (String) -> Unit,
) {
    var textFieldValue by remember {
        mutableStateOf(fieldMessage)
    }
    Column {
        Text(
            text = labelName,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp, bottom = 4.dp),
            textAlign = TextAlign.Start,
            color = Toast
        )
        Row() {
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = textFieldValue,
                enabled = fieldEnabled,
                readOnly = !fieldEnabled,
                onValueChange = {
                    textFieldValue = it
                    onValueChanged(it)
                },
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = LightBlue,
                    cursorColor = Black,
                    disabledLabelColor = LightBlue,
                    focusedIndicatorColor = Transparent,
                    unfocusedIndicatorColor = Transparent
                ),
                shape = RoundedCornerShape(8.dp),
                singleLine = true,
                trailingIcon = {
                    if (showClearButton) {
                        Icon(Icons.Default.Clear,
                            contentDescription = "clear text",
                            modifier = Modifier.clickable {
                                textFieldValue = ""
                            }
                        )
                    }
                }
            )
        }
    }
}

@Preview
@Composable
fun TextFieldWithLabelPreview() {
    TextFieldWithLabel(
        labelName = "LABEL",
        fieldMessage = "message",
        onValueChanged = {})
}

@Preview
@Composable
fun TextFieldWithLabelClearButtonPreview() {
    TextFieldWithLabel(
        labelName = "LABEL",
        fieldMessage = "message",
        showClearButton = true,
        onValueChanged = {})
}
