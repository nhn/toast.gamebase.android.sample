package com.toast.android.gamebase.sample.ui.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.ExposedDropdownMenuDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

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