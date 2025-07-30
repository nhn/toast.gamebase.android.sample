package com.toast.android.gamebase.sample.ui.components.dialog

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import com.toast.android.gamebase.sample.R

@Composable
fun ConfirmAlertDialog(
    isDialogOpened: Boolean,
    title: String,
    description: String,
    setDialogState: (Boolean) -> Unit,
    showCancel: Boolean = false,
    onOkButtonClicked: () -> Unit,
) {
    if (isDialogOpened) {
        AlertDialog(
            onDismissRequest = {
                setDialogState(false)
            },
            title = {
                Text(title)
            },
            text = {
                Text(description)
            },
            buttons = {
                DialogButtonRow(
                    showCancel = showCancel,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            bottom = dimensionResource(id = R.dimen.common_confirm_dialog_padding_bottom)
                        ),
                    onOkButtonClicked = {
                        onOkButtonClicked()
                        setDialogState(false)
                    },
                    onCancelButtonClicked = {
                        setDialogState(false)
                    },
                )
            },
            shape = RoundedCornerShape(
                dimensionResource(id = R.dimen.common_confirm_dialog_round_corner_radius))
        )
    }
}

@Preview
@Composable
fun PreviewConfirmDialog() {
    ConfirmAlertDialog(
        isDialogOpened = true,
        title = "제목",
        description = "설명",
        setDialogState = {},
        onOkButtonClicked = {}
    )
}

@Preview
@Composable
fun PreviewConfirmDialogWithCancel() {
    ConfirmAlertDialog(
        isDialogOpened = true,
        title = "제목",
        description = "설명",
        showCancel = true,
        setDialogState = {},
        onOkButtonClicked = {}
    )
}