package com.toast.android.gamebase.sample.ui.access_info

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.toast.android.gamebase.sample.R
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.toast.android.gamebase.sample.data.AccessInformation
import com.toast.android.gamebase.sample.data.AccessInformationRepository
import com.toast.android.gamebase.sample.ui.theme.*

@Composable
fun AccessInformationScreen(updateVersionInPreferenceAndState: () -> Unit) {
    val scrollState = rememberScrollState()

    Surface {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(White)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Column(
                modifier = Modifier.padding(top = 50.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.access_info_title),
                    color = Black,
                    fontSize = 30.sp,
                    fontWeight = FontWeight(400),
                    textAlign = TextAlign.Center
                )
                Text(
                    modifier = Modifier.padding(top = 20.dp),
                    text = stringResource(R.string.access_info_subtitle),
                    color = Grey500,
                    fontSize = 15.sp,
                    fontWeight = FontWeight(300),
                    textAlign = TextAlign.Center
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
            ) {
                Text(
                    text = stringResource(R.string.access_info_list_title),
                    color = Black,
                    fontSize = 25.sp,
                    fontWeight = FontWeight(350),
                    textAlign = TextAlign.Start
                )
                AccessInformationRepository().getAccessInformation().forEach { AccessInformation ->
                    ListAccessInformation(information = AccessInformation)
                }
            }
            Spacer(modifier = Modifier.height(70.dp))
            Button(
                modifier = Modifier
                    .height(70.dp)
                    .fillMaxSize(),
                onClick = updateVersionInPreferenceAndState,
                colors = ButtonDefaults.buttonColors(backgroundColor = Toast),
            ) {
                Text(
                    text = stringResource(R.string.button_ok),
                    color = White,
                    fontSize = 30.sp,
                    fontWeight = FontWeight(400),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
fun ListAccessInformation(information: AccessInformation) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Image(
            painter = painterResource(information.imgSrc),
            contentDescription = stringResource(information.contentDescription),
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .width(60.dp)
                .height(60.dp)
                .background(color = Grey300, shape = CircleShape)
                .padding(10.dp)
        )
        Column() {
            Text(
                text = stringResource(information.title),
                color = Black,
                fontSize = 15.sp,
                fontWeight = FontWeight(300)
            )
            Text(
                text = stringResource(information.subTitle),
                color = Grey500,
                fontSize = 13.sp,
                fontWeight = FontWeight(300)
            )
        }
    }
}