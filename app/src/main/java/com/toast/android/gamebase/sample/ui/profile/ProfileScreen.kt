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
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
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

    LaunchedEffect(Unit) {
        profileViewModel.updateData()
    }

    Column (modifier = Modifier
        .verticalScroll(scrollState)
        .padding(dimensionResource(id = R.dimen.profile_column_padding))){

        Image (
            painter = painterResource(id = R.drawable.profile),
            contentDescription = stringResource(R.string.profile_picture_content_description),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(dimensionResource(id = R.dimen.profile_image_size))
                .clip(CircleShape)
                .align(Alignment.CenterHorizontally))

        Spacer(Modifier.height(dimensionResource(id = R.dimen.profile_image_space_bottom)))

        Text(text = stringResource(R.string.profile_menu_gamebase_user_id), fontWeight = FontWeight.Bold)
        Text(text = profileViewModel.userId, color = Grey700)
        Spacer(Modifier.height(dimensionResource(id = R.dimen.profile_item_spacer)))

        Text(text = stringResource(R.string.profile_menu_gamebase_access_token), fontWeight = FontWeight.Bold)
        Text(text = profileViewModel.accessToken, color = Grey700)
        Spacer(Modifier.height(dimensionResource(id = R.dimen.profile_item_spacer)))

        Text(text = stringResource(R.string.profile_menu_last_logged_in_provider), fontWeight = FontWeight.Bold)
        Text(text = profileViewModel.lastLoggedInProvider, color = Grey700)
        Spacer(Modifier.height(dimensionResource(id = R.dimen.profile_item_spacer)))

        Text(text = stringResource(R.string.profile_menu_connected_idp_list), fontWeight = FontWeight.Bold)
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