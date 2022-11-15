package com.toast.android.gamebase.sample.ui.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import com.toast.android.gamebase.sample.R

@Composable
fun CopyrightFooter() {
    Spacer(modifier = Modifier.height(
        dimensionResource(id = R.dimen.common_copyright_footer_margin_top)))
    Text(
        fontSize = dimensionResource(
            id = R.dimen.common_copyright_footer_font_size).value.sp,
        color = Color.Gray,
        text = stringResource(id = R.string.copyright_nhn),
    )
    Spacer(modifier = Modifier.height(
        dimensionResource(id = R.dimen.common_copyright_footer_margin_bottom)))
}