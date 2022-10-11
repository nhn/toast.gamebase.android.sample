package com.nhn.android.gamebase.sample.ui.theme

import android.app.Activity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nhn.android.gamebase.sample.R
import com.nhn.android.gamebase.sample.ui.SplashActivity

@Composable
fun AccessInformationScreen(moveComposable: () -> Unit) {
    Surface {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(White),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Column(
                modifier = Modifier.padding(top = 50.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "앱 접근 권한 안내",
                    color = Black,
                    fontSize = 30.sp,
                    fontWeight = FontWeight(400),
                    textAlign = TextAlign.Center
                )
                Text(
                    modifier = Modifier.padding(top = 20.dp),
                    text = "Gamebase는 특정 상황에서 다음 권한들을 사용합니다.",
                    color = Grey500,
                    fontSize = 15.sp,
                    fontWeight = FontWeight(300),
                    textAlign = TextAlign.Center
                )
            }

            Column(modifier = Modifier.padding(24.dp)) {
                Text(
                    text = "선택적 접근 권한",
                    color = Black,
                    fontSize = 25.sp,
                    fontWeight = FontWeight(350),
                    textAlign = TextAlign.Start
                )

                LazyColumn() {
                    items(items = AccessInformationRepository().getAccessInformation()){AccessInformation ->
                        ListAccessInformation(information = AccessInformation)
                    }
                }
            }

            Spacer(modifier = Modifier.height(70.dp))

            Button(
                modifier = Modifier
                    .height(70.dp)
                    .fillMaxSize()
                    .weight(1f, false),
                onClick = moveComposable,
                colors = ButtonDefaults.buttonColors(backgroundColor = LightBlue400),
            ) {
                Text(
                    text = "확인",
                    color = White,
                    fontSize = 30.sp,
                    fontWeight = FontWeight(400),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

data class AccessInformation(val title : String, val subTitle : String, val imgSrc : Int)

class AccessInformationRepository() {
    fun getAccessInformation(): List<AccessInformation> {
        return listOf(
            AccessInformation("광고 식별자(선택)", "Gamebase IDEA API 호출 시 필요", R.drawable.person),
            AccessInformation(
                "카메라 (선택)",
                "Game 고객센터 API 호출 시 필요.\n사용자가 문의사항에 사진 또는 동영상 첨부시 사용.",
                R.drawable.photo_camera
            ),
            AccessInformation(
                "사진 (선택)",
                "Gamebase 고객센터 API 호출 시 필요.\n사용자가 문의사항에 앨범에서 사진 또는 동영상 첨부시 사용.",
                R.drawable.photo
            )
        )
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
            //contentDescription을 아래와 같이 정수형으로 두어도 될지 확인
            contentDescription = information.imgSrc.toString(),
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .width(60.dp)
                .height(60.dp)
                .background(color = Grey300, shape = CircleShape)
                .padding(10.dp)
        )
        Column() {
            Text(
                text = information.title,
                color = Black,
                fontSize = 15.sp,
                fontWeight = FontWeight(300)
            )
            Text(
                text = information.subTitle,
                color = Grey500,
                fontSize = 13.sp,
                fontWeight = FontWeight(300)
            )
        }
    }
}
