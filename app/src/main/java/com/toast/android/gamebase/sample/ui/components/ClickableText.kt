package com.toast.android.gamebase.sample.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import com.toast.android.gamebase.sample.R

@Composable
fun ClickableText(
    stringId: Int,
    onClick: () -> Unit
) {
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .padding(dimensionResource(id = R.dimen.setting_screen_menu_title_text_padding))
            .clickable() {
                onClick()
            }, text = stringResource(id = stringId)
    )
}