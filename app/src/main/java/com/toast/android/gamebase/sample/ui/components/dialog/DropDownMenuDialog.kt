package com.toast.android.gamebase.sample.ui.components.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Dialog
import com.toast.android.gamebase.sample.R
import com.toast.android.gamebase.sample.ui.components.input.DropdownMenuBox

@Composable
fun DropDownMenuDialog(
    title: String,
    isDialogOpened: Boolean,
    setDialogState: (Boolean) -> Unit,
    options: List<String>,
    modifier: Modifier = Modifier,
    onOkButtonClicked: (Int) -> Unit,
) {
    var expanded by remember {
        mutableStateOf(false)
    }
    var selected by remember {
        mutableStateOf(0)
    }

    if (isDialogOpened) {
        Dialog(onDismissRequest = { }) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth(80f)
                    .wrapContentHeight(),
                shape = RoundedCornerShape(
                    dimensionResource(id = R.dimen.drop_down_menu_box_dialog_round_corner_radius)
                ),
                color = MaterialTheme.colors.surface
            ) {
                Column(
                    modifier = Modifier.padding(
                        dimensionResource(id = R.dimen.drop_down_menu_box_dialog_column_padding)
                    ),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom =
                            dimensionResource(id = R.dimen.drop_down_menu_box_dialog_title_padding_bottom)
                            ),
                        text = title,
                        textAlign = TextAlign.Left
                    )
                    DropdownMenuBox(
                        options = options,
                        expanded = expanded,
                        onExpandChanged = { newState -> expanded = newState },
                        selected = selected,
                        onSelected = { newSelected ->
                            selected = newSelected
                        },
                        modifier = modifier
                    )
                    DialogButtonRow(
                        onOkButtonClicked = {
                            onOkButtonClicked(selected)
                            setDialogState(false)
                        },
                        onCancelButtonClicked = {
                            setDialogState(false)
                        },
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewDropDownMenuBoxDialog() {
    DropDownMenuDialog(
        title = "Select one",
        isDialogOpened = true,
        setDialogState = {},
        options = listOf("A", "B", "C"),
        modifier = Modifier,
        onOkButtonClicked = { }
    )
}
