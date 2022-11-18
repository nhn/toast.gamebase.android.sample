package com.toast.android.gamebase.sample.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.Switch
import androidx.compose.material.SwitchDefaults
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.toast.android.gamebase.sample.R
import com.toast.android.gamebase.sample.ui.theme.Toast

@Composable
fun SwitchWithLabel(
    stringId: Int,
    state: Boolean,
    enableSwitch: Boolean,
    event: (Boolean) -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxSize().heightIn(TextFieldDefaults.MinHeight),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(stringId),
            modifier = Modifier.padding(dimensionResource(id = R.dimen.common_switch_padding)),
            fontWeight = FontWeight.Normal
        )
        Switch(
            checked = state,
            enabled = enableSwitch,
            onCheckedChange = event,
            colors = SwitchDefaults.colors(checkedThumbColor = Toast)
        )
    }
}

@Composable
fun SwitchWithLabel(
    label: String,
    state: Boolean,
    enableSwitch: Boolean,
    event: (Boolean) -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth().heightIn(TextFieldDefaults.MinHeight),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            modifier = Modifier.padding(10.dp).weight(1f),
            fontWeight = FontWeight.Normal
        )
        Switch(
            modifier = Modifier.wrapContentWidth(),
            checked = state,
            enabled = enableSwitch,
            onCheckedChange = event,
            colors = SwitchDefaults.colors(checkedThumbColor = Toast)
        )
    }
}

@Preview
@Composable
fun SwitchLongLabelPreview() {
    var state by remember {
        mutableStateOf(false)
    }
    SwitchWithLabel(
        label = "verylongtitleverylongtitle" +
                "verylongtitleverylongtitlevery" +
                "longtitleverylongtitleverylongti" +
                "tleverylongtitleverylongtitleverylo" +
                "ngtitleverylongtitleverylongtitlever" +
                "ylongtitle",
        state = state,
        enableSwitch = true,
        event = { newState -> state = newState }
    )
}

@Preview
@Composable
fun SwitchShortLabelPreview() {
    var state by remember {
        mutableStateOf(false)
    }
    SwitchWithLabel(
        label = "a",
        state = state,
        enableSwitch = true,
        event = { newState -> state = newState }
    )
}
