package com.toast.android.gamebase.sample.ui.common

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.toast.android.gamebase.sample.R

@Composable
fun CopyrightFooter() {
    Spacer(modifier = Modifier.height(40.dp))
    Text(
        fontSize = 12.sp,
        color = Color.Gray,
        text = stringResource(id = R.string.copyright_nhn),
    )
    Spacer(modifier = Modifier.height(48.dp))
}