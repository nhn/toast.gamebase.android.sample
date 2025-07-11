package com.toast.android.gamebase.sample.ui.components.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.toast.android.gamebase.sample.R

@Composable
fun DialogButtonRow(
    showCancel: Boolean = true,
    modifier: Modifier = Modifier.fillMaxWidth(),
    onOkButtonClicked: () -> Unit,
    onCancelButtonClicked: () -> Unit,
    okButtonText: String = stringResource(id = R.string.button_ok),
    cancelButtonText: String = stringResource(id = R.string.button_cancel)
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        if (showCancel) {
            TextButton(
                onClick = onCancelButtonClicked
            ) {
                Text(cancelButtonText)
            }
        }
        TextButton(
            onClick = onOkButtonClicked
        ) {
            Text(okButtonText)
        }
    }
}