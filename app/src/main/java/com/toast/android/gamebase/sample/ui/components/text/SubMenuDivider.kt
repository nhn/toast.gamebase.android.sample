/*
 * © NHN Corp. All rights reserved.
 * NHN Corp. PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.toast.android.gamebase.sample.ui.components.text

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import com.toast.android.gamebase.sample.R

@Composable
fun SubMenuDivider(menuNameId: Int) {
    Spacer(modifier = Modifier.height(
        dimensionResource(id = R.dimen.common_submenu_divider_margin_top)
    ))
    Text(
        text = stringResource(menuNameId),
        style = MaterialTheme.typography.caption)
    Divider(modifier = Modifier.fillMaxWidth())
}

@Composable
fun SubMenuDivider(menuName: String) {
    Spacer(modifier = Modifier.height(
        dimensionResource(id = R.dimen.common_submenu_divider_margin_top)
    ))
    Text(
        text = menuName,
        style = MaterialTheme.typography.caption)
    Divider(modifier = Modifier.fillMaxWidth())
}
