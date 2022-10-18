package com.toast.android.gamebase.sample.ui.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.toast.android.gamebase.sample.GamebaseActivity
import com.toast.android.gamebase.sample.R
import com.toast.android.gamebase.sample.ui.common.ConfirmAlertDialog
import com.toast.android.gamebase.sample.ui.login.LoginState

@Composable
fun SettingsScreen(
    activity: GamebaseActivity,
    navController: NavController,
    settingsViewModel: SettingsViewModel = viewModel()
) {
    val isLogoutDialogOpened = remember { mutableStateOf(false) }
    val isWithdrawDialogOpened = remember { mutableStateOf(false) }

    LaunchedEffect(key1 = settingsViewModel.uiState) {
        if (settingsViewModel.uiState == LoginState.LOGGED_OUT) {
            settingsViewModel.navigateToLogin(navController)
        }
    }

    Column (modifier = Modifier.padding(24.dp)){
        Text(text = stringResource(R.string.setting_login_manage), modifier = Modifier.padding(6.dp), fontWeight = FontWeight.Bold)
        Text(text = stringResource(id = R.string.logout),
            modifier = Modifier
                .padding(6.dp)
                .clickable {
                    isLogoutDialogOpened.value = true
                })
        Text(text = stringResource(R.string.withdraw),
            modifier = Modifier
                .padding(6.dp)
                .clickable {
                    isWithdrawDialogOpened.value = true
                })
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


