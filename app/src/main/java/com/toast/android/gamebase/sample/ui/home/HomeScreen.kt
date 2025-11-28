package com.toast.android.gamebase.sample.ui.home

import android.app.Activity
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.toast.android.gamebase.sample.R
import com.toast.android.gamebase.sample.gamebase_manager.openContact
import com.toast.android.gamebase.sample.gamebase_manager.openGameNotices
import com.toast.android.gamebase.sample.gamebase_manager.showImageNotices

private const val TAG = "HomeScreen"
@Composable
fun HomeScreen(activity: Activity, onLoggedOut: () -> Unit, homeViewModel: HomeViewModel = viewModel()) {
    Surface {
        LaunchedEffect(Unit) {
            homeViewModel.setGamebaseEventHandler(activity = activity)
            homeViewModel.getTestDeviceInfo()
            homeViewModel.checkNewGameNotice()
            showImageNotices(activity) {}
        }
        if (homeViewModel.onKickOut) {
            onLoggedOut()
        }
        InnerHomeScreen(
            testDevice = homeViewModel.isTestDevice,
            matchingTypes = homeViewModel.matchingTypes,
            hasNewGameNotice = homeViewModel.hasNewGameNotice,
            onGameNoticeRead = { homeViewModel.markGameNoticeAsRead() }
        )
    }
}

@Composable
fun InnerHomeScreen(
    testDevice: Boolean,
    matchingTypes: String,
    hasNewGameNotice: Boolean = false,
    onGameNoticeRead: () -> Unit = {}
) {
    Box (
        modifier = Modifier.fillMaxSize(),
    ) {
        val activity = LocalContext.current as? Activity
        if (testDevice) {
            Text(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(dimensionResource(id = R.dimen.home_screen_test_device_text_padding)),
                color = Color.Red,
                fontWeight = FontWeight.Bold,
                text = "test device(${matchingTypes})",
                fontSize = dimensionResource(id = R.dimen.home_screen_test_device_text_size).value.sp
            )
        }
        Column(modifier = Modifier.wrapContentSize()
            .align(Alignment.TopEnd)
            .padding(
                horizontal = dimensionResource(id = R.dimen.home_screen_button_padding_horizontal),
                vertical = dimensionResource(id = R.dimen.home_screen_button_padding_vertical)
            )
        ) {
            IconButtonWithBadge(
                onClick = {
                    activity?.let {
                        openGameNotices(
                            activity = it,
                            onCloseCallback = { exception ->
                                Log.d(TAG, "Game Notice Exception: $exception")
                            }
                        )
                        onGameNoticeRead()
                    }
                },
                iconResId = R.drawable.game_notice,
                iconSize = dimensionResource(R.dimen.home_screen_button_image_size),
                buttonText = stringResource(R.string.home_game_notice_button),
                buttonTextFontSize = dimensionResource(R.dimen.home_screen_button_text_font_size),
                showBadge = hasNewGameNotice
            )
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.home_screen_button_space)))
            IconButton(
                onClick = {
                    activity?.let {
                        openContact(
                            activity = it,
                            configuration = null,
                            onClosedCallback = { exception ->
                                Log.d(TAG, "Contact Exception: $exception")
                            }
                        )
                    }
                },
                iconResId = R.drawable.contact,
                iconSize = dimensionResource(R.dimen.home_screen_button_image_size),
                buttonText = stringResource(R.string.home_contact_button),
                buttonTextFontSize = dimensionResource(R.dimen.home_screen_button_text_font_size)
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

@Composable
fun IconButton(
    onClick: () -> Unit,
    iconResId: Int,
    iconSize: Dp,
    buttonText: String,
    buttonTextFontSize: Dp,
) {
    Column(
        modifier = Modifier
            .width(dimensionResource(R.dimen.home_screen_button_size))
            .wrapContentHeight().heightIn(min = dimensionResource(R.dimen.home_screen_button_size))
            .clip(RoundedCornerShape(dimensionResource(R.dimen.home_screen_button_round_corner_shape_radius)))
            .clickable {
                onClick()
            },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(iconResId),
            contentDescription = buttonText,
            modifier = Modifier.size(iconSize)
        )
        Text(
            modifier = Modifier.padding(top = dimensionResource(R.dimen.home_screen_button_image_text_padding)),
            fontSize = buttonTextFontSize.value.sp,
            textAlign = TextAlign.Center,
            text = buttonText
        )
    }
}

@Composable
fun IconButtonWithBadge(
    onClick: () -> Unit,
    iconResId: Int,
    iconSize: Dp,
    buttonText: String,
    buttonTextFontSize: Dp,
    showBadge: Boolean = false,
) {
    Column(
        modifier = Modifier
            .width(dimensionResource(R.dimen.home_screen_button_size))
            .wrapContentHeight().heightIn(min = dimensionResource(R.dimen.home_screen_button_size))
            .clip(RoundedCornerShape(dimensionResource(R.dimen.home_screen_button_round_corner_shape_radius)))
            .clickable {
                onClick()
            },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box {
            Image(
                painter = painterResource(iconResId),
                contentDescription = buttonText,
                modifier = Modifier.size(iconSize)
            )
            if (showBadge) {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .background(Color.Red, CircleShape)
                        .align(Alignment.TopEnd)
                )
            }
        }
        Text(
            modifier = Modifier.padding(top = dimensionResource(R.dimen.home_screen_button_image_text_padding)),
            fontSize = buttonTextFontSize.value.sp,
            textAlign = TextAlign.Center,
            text = buttonText
        )
    }
}

@Preview
@Composable
fun PreviewHomeScreen() {
    InnerHomeScreen(true, "IP | Device")
}

@Preview(locale = "en")
@Composable
fun PreviewHomeScreen2() {
    InnerHomeScreen(true, "IP | Device")
}