package com.toast.android.gamebase.sample.ui.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.toast.android.gamebase.sample.GamebaseActivity
import com.toast.android.gamebase.sample.R
import com.toast.android.gamebase.sample.ui.theme.White

@Composable
fun SplashScreen(
    activity: GamebaseActivity,
    splashViewModel: SplashViewModel = viewModel(),
    onInitialized: () -> Unit
) {
    LaunchedEffect(Unit) {
        splashViewModel.initialize(activity)
    }

    LaunchedEffect(splashViewModel.isInitialized.value) {
        if (splashViewModel.isInitialized.value) {
            onInitialized()
        }
    }
    SplashSurface()
}

@Composable
fun SplashSurface() {
    Surface {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(White),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Box(
                modifier = Modifier
                    .width(
                        dimensionResource(id = R.dimen.splash_image_width)
                    ),
                contentAlignment = Alignment.Center,
            ) {
                Image(
                    painter = painterResource(id = R.drawable.gamebase_logo),
                    contentDescription = stringResource(
                        id = R.string.gamebase_splash_content_description)
                )
            }
        }
    }
}

@Preview
@Composable
fun PreviewSplash() {
    SplashSurface()
}