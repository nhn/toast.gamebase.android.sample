package com.toast.android.gamebase.sample.ui.main

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.toast.android.gamebase.sample.R
import com.toast.android.gamebase.sample.ui.navigation.screens


@Composable
fun MainDrawer(
    onDestinationClicked: (route: String) -> Unit
) {
    Column(
        Modifier
            .wrapContentWidth()
            .padding(
                horizontal = dimensionResource(id = R.dimen.main_screen_drawer_padding_horizontal),
                vertical = dimensionResource(id = R.dimen.main_screen_drawer_padding_vertical)
            )
    ) {
        screens.forEach { screen ->
            Column (
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onDestinationClicked(screen.route)
                    }
                    .padding(
                        dimensionResource(id = R.dimen.main_screen_screen_menu_padding)
                    )
            ){
                Text(
                    text = stringResource(screen.resourceId),
                    style = MaterialTheme.typography.body1,
                )
            }
        }
    }
}

@Preview
@Composable
fun DrawablePreview() {
    MainDrawer {}
}
