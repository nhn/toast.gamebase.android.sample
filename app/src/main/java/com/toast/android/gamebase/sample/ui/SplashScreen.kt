package com.toast.android.gamebase.sample.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.toast.android.gamebase.sample.R
import com.toast.android.gamebase.sample.ui.theme.Black
import com.toast.android.gamebase.sample.ui.theme.LightBlue400
import com.toast.android.gamebase.sample.ui.theme.White

@Composable
fun LaunchingScreen() {
    Surface {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(White),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,

            ) {
            Text(
                text = "Gamebase SampleApplication",
                color = Black,
                fontSize = 30.sp,
                fontWeight = FontWeight(400),
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.height(70.dp))
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center,
            ) {
                Image(painter = painterResource(id = R.drawable.splash), contentDescription = "splash")
            }
        }
    }
}