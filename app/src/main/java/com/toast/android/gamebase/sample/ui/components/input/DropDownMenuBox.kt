package com.toast.android.gamebase.sample.ui.components.input

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.heightIn
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.ExposedDropdownMenuDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
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
        modifier = Modifier.fillMaxWidth().heightIn(TextFieldDefaults.MinHeight),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            modifier = Modifier.padding(
                dimensionResource(id = R.dimen.setting_screen_menu_title_text_padding)))
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

@Preview
@Composable
fun PreviewDropdownMenuBoxWithTitle() {
    DropdownMenuBoxWithTitle(
        title = "title",
        options = listOf("1", "2", "3"),
        expanded = false,
        onExpandChanged = {},
        selected = 1,
        onSelected = {},
        modifier = Modifier
    )
}
