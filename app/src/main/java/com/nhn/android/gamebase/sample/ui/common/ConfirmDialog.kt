package com.nhn.android.gamebase.sample.ui.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ConfirmAlertDialog(
    dialogOpened: Boolean,
    title: String,
    description: String,
    setDialogStatus: (Boolean) -> Unit,
    onOkButtonClicked: () -> Unit,
) {
    if (dialogOpened) {
        AlertDialog(
            onDismissRequest = {
                setDialogStatus(false)
            },
            title = {
                Text(title)
            },
            text = {
                Text(description)
            },
            buttons = {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 20.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly) {
                    TextButton(
                        onClick = {
                            onOkButtonClicked()
                            setDialogStatus(false)
                        }
                    ) {
                        Text("확인")
                    }
                    TextButton(
                        onClick = {
                            setDialogStatus(false)
                        }
                    ) {
                        Text("취소")
                    }
                }
            },
            shape = RoundedCornerShape(24.dp)
        )
    }
}