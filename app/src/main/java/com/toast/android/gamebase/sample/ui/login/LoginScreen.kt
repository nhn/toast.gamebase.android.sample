package com.toast.android.gamebase.sample.ui.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.toast.android.gamebase.sample.GamebaseActivity
import com.toast.android.gamebase.sample.R
import com.toast.android.gamebase.sample.getIconResourceById
import com.toast.android.gamebase.sample.supportedIdpList
import com.toast.android.gamebase.sample.ui.common.CopyrightFooter
import com.toast.android.gamebase.sample.ui.theme.GamebaseSampleProjectTheme

@Composable
fun LoginScreen(
    loginViewModel: LoginViewModel = viewModel(),
    navigateToHome: () -> Unit) {

    val gamebaseActivity = LocalContext.current as GamebaseActivity

    LaunchedEffect(true) {
        loginViewModel.tryLastIdpLogin(gamebaseActivity)
    }

    LaunchedEffect(loginViewModel.uiState) {
        if (loginViewModel.uiState == LoginState.LOGGED_IN) {
            navigateToHome()
        }
    }

    GamebaseSampleProjectTheme {
        Surface(Modifier.background(Color.White)) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                item {
                    Spacer(modifier = Modifier.height(40.dp))
                    Text(
                        style = MaterialTheme.typography.h4,
                        text = stringResource(R.string.login_title)
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(stringResource(R.string.login_description))
                    Spacer(modifier = Modifier.height(40.dp))
                }
                itemsIndexed(supportedIdpList) { _, idp ->
                    OutlineLoginButton(loginViewModel, idp)
                    Spacer(modifier = Modifier.height(4.dp))
                }
                item {
                    CopyrightFooter()
                }
            }
        }
    }
}

@Composable
fun OutlineLoginButton(loginViewModel: LoginViewModel, idp: String) {
    val gamebaseActivity = LocalContext.current as GamebaseActivity
    OutlinedButton(
        modifier = Modifier
            .fillMaxWidth(0.9f),
        colors = ButtonDefaults.buttonColors(
            contentColor = Color.Black,
            backgroundColor = Color.White
        ),
        onClick = {
            loginViewModel.login(gamebaseActivity, idp)
        }) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Image(
                painter = painterResource(getIconResourceById(idp)),
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
