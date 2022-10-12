package com.nhn.android.gamebase.sample.ui.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.nhn.android.gamebase.sample.GamebaseActivity
import com.nhn.android.gamebase.sample.ui.common.ConfirmAlertDialog
import com.nhn.android.gamebase.sample.ui.login.LoginState

@Composable
fun SettingsScreen(
    activity: GamebaseActivity,
    navController: NavController,
    settingsViewModel: SettingsViewModel = viewModel()
) {
    val isDialogOpened = remember { mutableStateOf(false) }

    LaunchedEffect(key1 = settingsViewModel.uiState) {
        if (settingsViewModel.uiState == LoginState.LOGGED_OUT) {
            settingsViewModel.navigateToLogin(navController)
        }
    }

    Column (modifier = Modifier.padding(24.dp)){
        Text(text = "계정 관리", modifier = Modifier.padding(6.dp), fontWeight = FontWeight.Bold)
        Text(text = "로그아웃",
            modifier = Modifier
                .padding(6.dp)
                .clickable {
                    isDialogOpened.value = true
                })
        Text(text = "탈퇴하기",
            modifier = Modifier.padding(6.dp))
        ConfirmAlertDialog(
            isDialogOpened.value,
            "Gamebase 로그아웃",
            "로그아웃 하시겠습니까?",
            { opened ->
                isDialogOpened.value = opened
            },
            {
                settingsViewModel.logout(activity)
            }
        )
    }
}


