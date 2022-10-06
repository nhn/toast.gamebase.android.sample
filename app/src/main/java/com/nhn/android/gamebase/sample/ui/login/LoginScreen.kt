package com.nhn.android.gamebase.sample.ui.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.scrollable
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.nhn.android.gamebase.sample.R
import com.nhn.android.gamebase.sample.ui.theme.GamebaseSampleProjectTheme

@Composable
fun LoginScreen(
    loginViewModel: LoginViewModel = viewModel(),
    onLoginSuccess: () -> Unit) {
    GamebaseSampleProjectTheme {
        val scrollState = rememberScrollState()
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
                for (idp in loginViewModel.supportedIdpList) {
                    OutlineLoginButton(loginViewModel, idp, onLoginSuccess)
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
fun OutlineLoginButton(viewModel: LoginViewModel, idp: String, onLoginSuccess: () -> Unit) {
    OutlinedButton(
        modifier = Modifier
            .fillMaxWidth(0.9f),
        colors = ButtonDefaults.buttonColors(
            contentColor = Color.Black,
            backgroundColor = Color.White
        ),
        onClick = { viewModel.login(idp, onLoginSuccess) }) {
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

fun getIconResourceById(idp: String): Int {
    when(idp){
        "guest" -> return R.drawable.guest_logo
        "google" -> return R.drawable.google_logo
        "naver" -> return R.drawable.naver_logo
        "applieid" -> return R.drawable.appleid_logo
        "facebook" -> return R.drawable.facebook_logo
        "kakaogame" -> return R.drawable.kakaogames_logo
        "line" -> return R.drawable.line_logo
        "twitter" -> return R.drawable.twitter_logo
        "weibo" -> return R.drawable.weibo_logo
        "payco" -> return R.drawable.payco_logo
    }
    return R.drawable.guest_logo
}

@Preview
@Composable
fun PreviewLoginScreen() {
    LoginScreen(){}
}