/*
 * © NHN Corp. All rights reserved.
 * NHN Corp. PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.toast.android.gamebase.sample.ui.components.input

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
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
    onValueChanged: (String) -> Unit,
) {
    Column {
        Text(
            text = labelName,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp, bottom = 4.dp),
            textAlign = TextAlign.Start,
            color = Toast
        )
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = fieldMessage,
            enabled = fieldEnabled,
            readOnly = !fieldEnabled,
            onValueChange = onValueChanged,
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = LightBlue,
                cursorColor = Black,
                disabledLabelColor = LightBlue,
                focusedIndicatorColor = Transparent,
                unfocusedIndicatorColor = Transparent
            ),
            shape = RoundedCornerShape(8.dp),
            singleLine = true
        )
    }
}
