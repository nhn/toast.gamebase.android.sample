package com.toast.android.gamebase.sample.ui.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.toast.android.gamebase.sample.R
import com.toast.android.gamebase.sample.ui.theme.Grey700

@Composable
fun ProfileScreen(
    profileViewModel: ProfileViewModel = viewModel()
) {
    val scrollState = rememberScrollState()

    // https://developer.android.com/jetpack/compose/side-effects#disposableeffect
    // TODO: onResume 에서 데이터 갱신
    LaunchedEffect(profileViewModel) {
        profileViewModel.updateData()
    }
    Column (modifier = Modifier
        .padding(30.dp)
        .verticalScroll(scrollState)){

        Image (
            painter = painterResource(id = R.drawable.profile),
            contentDescription = "profile picture",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(180.dp)
                .clip(CircleShape)
                .align(Alignment.CenterHorizontally))

        Spacer(Modifier.height(40.dp))

        Text(text = "Gamebase UserID", fontWeight = FontWeight.Bold)
        Text(text = profileViewModel.userId, color = Grey700)
        Spacer(Modifier.height(20.dp))

        Text(text = "Gamebase AccessToken", fontWeight = FontWeight.Bold)
        Text(text = profileViewModel.accessToken, color = Grey700)
        Spacer(Modifier.height(20.dp))

        Text(text = "Last Logged in Provider", fontWeight = FontWeight.Bold)
        Text(text = profileViewModel.lastLoggedInProvider, color = Grey700)
        Spacer(Modifier.height(20.dp))

        Text(text = "Idp 연동 목록", fontWeight = FontWeight.Bold)
        for (idp in profileViewModel.connectedIdpList) {
            Text(text = idp, color = Grey700)
        }
    }
}

@Preview(widthDp = 360, heightDp = 720)
@Composable
fun ProfilePreview() {
    ProfileScreen()
}