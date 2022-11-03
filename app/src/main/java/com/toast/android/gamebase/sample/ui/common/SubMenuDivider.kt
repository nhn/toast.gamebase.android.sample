package com.toast.android.gamebase.sample.ui.common

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

@Composable
fun SubMenuDivider(menuNameId: Int) {
    Spacer(modifier = Modifier.height(12.dp))
    Text(
        text = stringResource(menuNameId),
        style = MaterialTheme.typography.caption)
    Divider(modifier = Modifier.fillMaxWidth())
}

@Composable
fun SubMenuDivider(menuName: String) {
    Spacer(modifier = Modifier.height(12.dp))
    Text(
        text = menuName,
        style = MaterialTheme.typography.caption)
    Divider(modifier = Modifier.fillMaxWidth())
}