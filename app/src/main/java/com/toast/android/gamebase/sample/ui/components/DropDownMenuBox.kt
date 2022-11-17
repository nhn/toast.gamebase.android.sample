package com.toast.android.gamebase.sample.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.ExposedDropdownMenuDefaults
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
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Dialog
import com.toast.android.gamebase.sample.R

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DropdownMenuBox(
    options: List<String>,
    expanded: Boolean,
    onExpandChanged: (Boolean) -> Unit,
    selected: Int,
    onSelected: (Int) -> Unit,
    modifier: Modifier
) {
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = onExpandChanged,
        modifier = modifier
    ) {
        OutlinedTextField(
            readOnly = true,
            value = options[selected],
            onValueChange = { },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = expanded
                )
            },
            colors = ExposedDropdownMenuDefaults.textFieldColors(
                backgroundColor = MaterialTheme.colors.surface
            )
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                onExpandChanged(false)
            }
        ) {
            options.forEachIndexed { index, selectionOption ->
                DropdownMenuItem(
                    onClick = {
                        onSelected(index)
                        onExpandChanged(false)
                    }
                ) {
                    Text(text = selectionOption)
                }
            }
        }
    }
}

@Composable
fun DropdownMenuBoxWithTitle(
    title: String,
    options: List<String>,
    expanded: Boolean,
    onExpandChanged: (Boolean) -> Unit,
    selected: Int,
    onSelected: (Int) -> Unit,
    modifier: Modifier
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = title)
        DropdownMenuBox(
            options = options,
            expanded = expanded,
            onExpandChanged = onExpandChanged,
            selected = selected,
            onSelected = onSelected,
            modifier = modifier
        )
    }
}

@Composable
fun DropDownMenuBoxDialog(
    title: String,
    isDialogOpened: Boolean,
    setDialogStatus: (Boolean) -> Unit,
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
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        TextButton(
                            onClick = {
                                onOkButtonClicked(selected)
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
fun PreviewDropDownMenuBoxDialog() {
    DropDownMenuBoxDialog(
        title = "Select one",
        isDialogOpened = true,
        setDialogStatus = {},
        options = listOf("A", "B", "C"),
        modifier = Modifier,
        onOkButtonClicked = { selected -> }
    )
}
