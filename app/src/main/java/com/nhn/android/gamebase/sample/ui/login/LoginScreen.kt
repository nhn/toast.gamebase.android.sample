package com.nhn.android.gamebase.sample.ui.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.nhn.android.gamebase.sample.GamebaseActivity
import com.nhn.android.gamebase.sample.GamebaseManager
import com.nhn.android.gamebase.sample.R
import com.nhn.android.gamebase.sample.ui.navigation.SampleAppScreens
import com.nhn.android.gamebase.sample.ui.theme.GamebaseSampleProjectTheme
import com.toast.android.gamebase.Gamebase
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    activity: GamebaseActivity,
    loginViewModel: LoginViewModel = viewModel(),
    navController: NavController) {

    LaunchedEffect(true) {
        loginViewModel.tryLastIdpLogin(activity)
    }

    LaunchedEffect(loginViewModel.uiState) {
        if (loginViewModel.uiState == LoginState.LOGGED_IN) {
            loginViewModel.navigateToHome(navController)
        }
    }

    val scrollState = rememberScrollState()
    GamebaseSampleProjectTheme {
        Surface(Modifier.background(Color.White)) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Spacer(modifier = Modifier.height(40.dp))
                Text(
                    style = MaterialTheme.typography.h4,
                    text = "Gamebase 로그인"
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text("원하시는 로그인 타입을 선택해 주세요.")
                Spacer(modifier = Modifier.height(40.dp))
                for (idp in supportedIdpList) {
                    OutlineLoginButton(activity, loginViewModel, idp)
                    Spacer(modifier = Modifier.height(4.dp))
                }
                Spacer(modifier = Modifier.weight(1.0f))
                Text(
                    fontSize = 12.sp,
                    color = Color.Gray,
                    text = "Copyright NHN Corp All Rights reserved.",
                )
                Spacer(modifier = Modifier.height(48.dp))
            }
        }
    }
}

@Composable
fun OutlineLoginButton(activity: GamebaseActivity, loginViewModel: LoginViewModel, idp: String) {
    OutlinedButton(
        modifier = Modifier
            .fillMaxWidth(0.9f),
        colors = ButtonDefaults.buttonColors(
            contentColor = Color.Black,
            backgroundColor = Color.White
        ),
        onClick = {
            loginViewModel.login(activity, idp)
        }) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Image(
                painter = painterResource(loginViewModel.getIconResourceById(idp)),
                contentDescription = idp,
                modifier = Modifier
                    .width(30.dp)
                    .height(30.dp)
                    .padding(4.dp)
            )
            Spacer(Modifier.width(12.dp))
            Text(
                color = Color.Black,
                text = "$idp 로 로그인",
                textAlign = TextAlign.Center
            )
        }
    }
}
