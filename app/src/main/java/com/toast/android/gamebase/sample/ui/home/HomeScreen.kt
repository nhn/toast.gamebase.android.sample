package com.toast.android.gamebase.sample.ui.home

import android.app.Activity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.toast.android.gamebase.sample.R
import com.toast.android.gamebase.sample.gamebase_manager.showImageNotices

@Composable
fun HomeScreen(activity: Activity, onLoggedOut: () -> Unit, homeViewModel: HomeViewModel = viewModel()) {
    Surface {
        LaunchedEffect(Unit) {
            homeViewModel.setGamebaseEventHandler(activity = activity)
            homeViewModel.getTestDeviceInfo()
            showImageNotices(activity) {}
        }
        if (homeViewModel.onKickOut) {
            onLoggedOut()
        }
        InnerHomeScreen(homeViewModel.isTestDevice, homeViewModel.matchingTypes)
    }
}

@Composable
fun InnerHomeScreen(testDevice: Boolean, matchingTypes: String) {
    Box (
        modifier = Modifier.fillMaxSize(),
    ) {
        if (testDevice) {
            Text(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(dimensionResource(id = R.dimen.home_screen_test_device_text_padding)),
                color = Color.Red,
                fontWeight = FontWeight.Bold,
                text = "test device(${matchingTypes})",
                fontSize = dimensionResource(id = R.dimen.home_screen_test_device_text_size).value.sp
            )
        }
        Text(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(dimensionResource(id = R.dimen.home_screen_guide_text_padding)),
            text = stringResource(id = R.string.home_main_text)
        )
    }
}

@Preview
@Composable
fun PreviewHomeScreen() {
    InnerHomeScreen(true, "IP | Device")
}