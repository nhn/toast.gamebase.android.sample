package com.toast.android.gamebase.sample.ui.common

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CopyrightFooter() {
    Spacer(modifier = Modifier.height(40.dp))
    Text(
        fontSize = 12.sp,
        color = Color.Gray,
        text = "Copyright NHN Corp All Rights reserved.",
    )
    Spacer(modifier = Modifier.height(48.dp))
}