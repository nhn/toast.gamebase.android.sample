package com.toast.android.gamebase.sample.ui.settings

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.toast.android.gamebase.Gamebase
import com.toast.android.gamebase.sample.GamebaseActivity
import com.toast.android.gamebase.sample.R
import com.toast.android.gamebase.sample.ui.common.ConfirmAlertDialog
import com.toast.android.gamebase.sample.ui.common.NotificationWithSwitch
import com.toast.android.gamebase.sample.ui.login.LoginState
import com.toast.android.gamebase.sample.ui.theme.Black

@Composable
fun SettingsScreen(
    activity: GamebaseActivity,
    navController: NavController,
    settingsViewModel: SettingsViewModel = viewModel()
) {
    val scrollState = rememberScrollState()
    val isLogoutDialogOpened = remember { mutableStateOf(false) }
    val isWithdrawDialogOpened = remember { mutableStateOf(false) }

    LaunchedEffect(key1 = settingsViewModel.uiState) {
        if (settingsViewModel.uiState == LoginState.LOGGED_OUT) {
            settingsViewModel.navigateToLogin(navController)
        }
    }

    LaunchedEffect(Unit) {
        // TODO : 로그인 성공 이후 preference에서 푸시 관련 동의 내용 registerPush 메서드 호출로 활성화
        // 아래 url 주의 사항 확인
        // https://docs.toast.com/ko/Game/Gamebase/ko/aos-push/#register-push
        //settingsViewModel.registerPush(activity, "default")
        settingsViewModel.requestPushStatus(activity)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.Start
    ) {
        Column() {
            Text(
                text = stringResource(R.string.setting_version_sdk_title),
                modifier = Modifier.padding(6.dp),
                fontWeight = FontWeight.Bold
            )
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(R.string.setting_version_title),
                    modifier = Modifier.padding(6.dp),
                    fontWeight = FontWeight.Normal
                )
                Text(
                    text = Gamebase.getSDKVersion().toString(),
                    modifier = Modifier.padding(6.dp),
                    fontWeight = FontWeight.Normal
                )
            }
        }
        Divider(color = Black, thickness = 1.dp)
        Column() {
            Text(
                text = stringResource(R.string.setting_account_title),
                modifier = Modifier.padding(6.dp),
                fontWeight = FontWeight.Bold
            )
            // TODO : idp 계정 연동 구현 완료시 event 추가
            ListItem(R.string.setting_account_connected_idp_title) {
                Log.d("SettingScreen", "idp 계정 연동 listen 활성화")
            }
            ListItem(R.string.logout) {
                isLogoutDialogOpened.value = true
            }
            ListItem(R.string.withdraw) {
                isWithdrawDialogOpened.value = true
            }
        }
        Divider(color = Black, thickness = 1.dp)
        Column() {
            Text(
                text = stringResource(R.string.setting_notification_title),
                modifier = Modifier.padding(6.dp),
                fontWeight = FontWeight.Bold
            )
            NotificationWithSwitch(
                stringId = R.string.setting_normal_push_title,
                state = settingsViewModel.pushState,
                enableSwitch = true
            ) {
                settingsViewModel.registerPush(activity, PUSH_TYPE.NORMAL_PUSH)
            }
            NotificationWithSwitch(
                stringId = R.string.setting_advertising_push_title,
                state = settingsViewModel.advertisePushState,
                enableSwitch = settingsViewModel.pushState.value
            ) {
                settingsViewModel.registerPush(activity, PUSH_TYPE.ADVERTISING_PUSH)
            }
            NotificationWithSwitch(
                stringId = R.string.setting_night_advertising_push_title,
                state = settingsViewModel.nightAdvertisePushState,
                enableSwitch = (settingsViewModel.pushState.value && settingsViewModel.advertisePushState.value)
            ) {
                settingsViewModel.registerPush(activity, PUSH_TYPE.NIGHT_ADVERTISING_PUSH)
            }
            NotificationWithSwitch(
                stringId = R.string.setting_push_foreground_title,
                state = settingsViewModel.foregroundState,
                enableSwitch = settingsViewModel.pushState.value
            ) {
                settingsViewModel.registerPushForeground(activity)
            }
        }

        Divider(color = Black, thickness = 1.dp)

        Column() {
            Text(
                text = stringResource(R.string.setting_extra_title),
                modifier = Modifier.padding(6.dp),
                fontWeight = FontWeight.Bold
            )
            ListItem(R.string.setting_service_center_title) {
                settingsViewModel.loadServiceCenter(activity, "Test User")
            }
        }
        ConfirmAlertDialog(
            isLogoutDialogOpened.value,
            stringResource(id = R.string.setting_logout_dialog_title),
            stringResource(id = R.string.setting_logout_dialog_description),
            { opened -> isLogoutDialogOpened.value = opened },
            { settingsViewModel.logout(activity) }
        )
        ConfirmAlertDialog(
            isWithdrawDialogOpened.value,
            stringResource(id = R.string.setting_withdraw_dialog_title),
            stringResource(id = R.string.setting_withdraw_dialog_description),
            { opened -> isWithdrawDialogOpened.value = opened },
            { settingsViewModel.withdraw(activity) }
        )
    }
}

@Composable
private fun ListItem(stringId: Int, event: () -> Unit) {
    Text(
        text = stringResource(stringId),
        modifier = Modifier
            .fillMaxSize()
            .padding(6.dp)
            .clickable {
                event()
            },
        fontWeight = FontWeight.Normal
    )
}


