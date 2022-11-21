package com.toast.android.gamebase.sample.ui.settings

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.toast.android.gamebase.Gamebase
import com.toast.android.gamebase.sample.GamebaseActivity
import com.toast.android.gamebase.sample.R
import com.toast.android.gamebase.sample.ui.components.dialog.ConfirmAlertDialog
import com.toast.android.gamebase.sample.ui.components.text.SubMenuDivider
import com.toast.android.gamebase.sample.ui.components.input.SwitchWithLabel
import com.toast.android.gamebase.sample.ui.login.LoginState

@Composable
fun SettingsScreen(
    activity: GamebaseActivity,
    settingsViewModel: SettingsViewModel = viewModel(),
    onLoggedOut: () -> Unit,
    navigateToIdpMapping: () -> Unit,
) {
    val scrollState = rememberScrollState()
    val isLogoutDialogOpened = remember { mutableStateOf(false) }
    val isWithdrawDialogOpened = remember { mutableStateOf(false) }

    LaunchedEffect(key1 = settingsViewModel.uiState) {
        if (settingsViewModel.uiState == LoginState.LOGGED_OUT) {
            onLoggedOut()
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
            .padding(
                horizontal = dimensionResource(id = R.dimen.setting_screen_column_padding_horizontal)
            )
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.Start
    ) {
        Column() {
            SubMenuDivider(R.string.setting_version_sdk_title)
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(R.string.setting_version_title),
                    modifier = Modifier.padding(
                        dimensionResource(id = R.dimen.setting_screen_menu_title_text_padding)
                    ),
                    fontWeight = FontWeight.Normal
                )
                Text(
                    text = Gamebase.getSDKVersion().toString(),
                    modifier = Modifier.padding(
                        dimensionResource(id = R.dimen.setting_screen_menu_description_text_padding)),
                    fontWeight = FontWeight.Normal
                )
            }
        }

        Column() {
            SubMenuDivider(R.string.setting_account_title)
            // TODO : idp 계정 연동 구현 완료시 event 추가
            ListItem(R.string.setting_account_connected_idp_title) {
                navigateToIdpMapping()
                Log.d("SettingScreen", "idp 계정 연동 listen 활성화")
            }
            ListItem(R.string.logout) {
                isLogoutDialogOpened.value = true
            }
            ListItem(R.string.withdraw) {
                isWithdrawDialogOpened.value = true
            }
        }

        Column() {
            SubMenuDivider(R.string.setting_notification_title)
            SwitchWithLabel(
                stringId = R.string.setting_normal_push_title,
                state = settingsViewModel.pushState.value,
                enableSwitch = true
            ) { newState ->
                settingsViewModel.pushState.value = newState
                settingsViewModel.registerPush(activity, PUSH_TYPE.NORMAL_PUSH)
            }
            SwitchWithLabel(
                stringId = R.string.setting_advertising_push_title,
                state = settingsViewModel.advertisePushState.value,
                enableSwitch = settingsViewModel.pushState.value
            ) { newState ->
                settingsViewModel.advertisePushState.value = newState
                settingsViewModel.registerPush(activity, PUSH_TYPE.ADVERTISING_PUSH)
            }
            SwitchWithLabel(
                stringId = R.string.setting_night_advertising_push_title,
                state = settingsViewModel.nightAdvertisePushState.value,
                enableSwitch = (settingsViewModel.pushState.value && settingsViewModel.advertisePushState.value)
            ) { newState ->
                settingsViewModel.nightAdvertisePushState.value = newState
                settingsViewModel.registerPush(activity, PUSH_TYPE.NIGHT_ADVERTISING_PUSH)
            }
            SwitchWithLabel(
                stringId = R.string.setting_push_foreground_title,
                state = settingsViewModel.foregroundState.value,
                enableSwitch = settingsViewModel.pushState.value
            ) { newState ->
                settingsViewModel.foregroundState.value = newState
                settingsViewModel.registerPushForeground(activity)
            }
        }

        Column() {
            SubMenuDivider(R.string.setting_extra_title)
            ListItem(R.string.setting_service_center_title) {
                settingsViewModel.loadServiceCenter(activity, "Test User")
            }
        }
        ConfirmAlertDialog(
            isDialogOpened = isLogoutDialogOpened.value,
            title = stringResource(id = R.string.setting_logout_dialog_title),
            description = stringResource(id = R.string.setting_logout_dialog_description),
            setDialogStatus = { newState -> isLogoutDialogOpened.value = newState },
            showCancel = true,
            onOkButtonClicked = { settingsViewModel.logout(activity) }
        )
        ConfirmAlertDialog(
            isDialogOpened = isWithdrawDialogOpened.value,
            title = stringResource(id = R.string.setting_withdraw_dialog_title),
            description = stringResource(id = R.string.setting_withdraw_dialog_description),
            setDialogStatus = { newState -> isWithdrawDialogOpened.value = newState },
            showCancel = true,
            onOkButtonClicked = { settingsViewModel.withdraw(activity) }
        )
    }
}

@Composable
private fun ListItem(stringId: Int, event: () -> Unit) {
    Text(
        text = stringResource(stringId),
        modifier = Modifier
            .fillMaxSize()
            .clickable {
                event()
            }
            .padding(
                dimensionResource(id = R.dimen.setting_screen_list_item_text_padding)
            ),
        fontWeight = FontWeight.Normal
    )
}
