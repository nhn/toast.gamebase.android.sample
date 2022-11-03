package com.toast.android.gamebase.sample.ui.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Switch
import androidx.compose.material.SwitchDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.toast.android.gamebase.sample.ui.theme.Toast

@Composable
fun SwitchWithLabel(
    stringId: Int,
    state: MutableState<Boolean>,
    enableSwitch: Boolean,
    event: (Boolean) -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(stringId),
            modifier = Modifier.padding(6.dp),
            fontWeight = FontWeight.Normal
        )
        Switch(
            checked = state.value,
            enabled = enableSwitch,
            onCheckedChange = event,
            colors = SwitchDefaults.colors(checkedThumbColor = Toast)
        )
    }
}