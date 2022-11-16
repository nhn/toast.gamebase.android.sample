package com.toast.android.gamebase.sample.ui.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.toast.android.gamebase.sample.R

@Composable
fun ClickableText(
    stringId: Int,
    onClick: () -> Unit
) {
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .clickable() {
                onClick()
            }
            .padding(dimensionResource(id = R.dimen.setting_screen_menu_title_text_padding)),
        text = stringResource(id = stringId)
    )
}

@Preview
@Composable
fun PreviewClickableText() {
    ClickableText(stringId = R.string.button_ok) {

    }
}