/*
 * © NHN Corp. All rights reserved.
 * NHN Corp. PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.toast.android.gamebase.sample.ui.components.text

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import com.toast.android.gamebase.sample.R

@Composable
fun ClickableText(
    stringId: Int,
    style: TextStyle = LocalTextStyle.current,
    onClick: () -> Unit
) {
    ClickableText(
        text = stringResource(id = stringId),
        onClick = onClick,
        style = style
    )
}

@Composable
fun ClickableText(
    text: String,
    style: TextStyle = LocalTextStyle.current,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .heightIn(TextFieldDefaults.MinHeight)
            .fillMaxWidth()
            .clickable() {
                onClick()
            }
            .padding(dimensionResource(id = R.dimen.setting_screen_menu_title_text_padding)),
        contentAlignment = Alignment.CenterStart
    ) {
        Text(
            text = text,
            style = style
        )
    }
}

@Preview
@Composable
fun PreviewClickableText() {
    ClickableText(stringId = R.string.button_ok) {}
}