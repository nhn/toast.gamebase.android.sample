package com.toast.android.gamebase.sample.ui.profile

import android.app.Activity
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.toast.android.gamebase.sample.R
import com.toast.android.gamebase.sample.ui.components.text.ClickableText
import com.toast.android.gamebase.sample.util.copyClipBoard

@Composable
fun ProfileScreen(
    activity: Activity = LocalContext.current as Activity,
    profileViewModel: ProfileViewModel = viewModel()
) {
    val scrollState = rememberScrollState()

    LaunchedEffect(Unit) {
        profileViewModel.updatePushToken(activity)
        profileViewModel.updateLastLoggedInProvider(activity)
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
        val context = LocalContext.current
        Text(text = stringResource(R.string.profile_menu_gamebase_user_id), fontWeight = FontWeight.Bold)
        ClickableText(
            text = profileViewModel.userId,
            showPadding = false,
            style = MaterialTheme.typography.body1,
            onClick = { copyClipBoard(context, profileViewModel.userId) })
        Spacer(Modifier.height(dimensionResource(id = R.dimen.profile_item_spacer)))

        Text(text = stringResource(R.string.profile_menu_gamebase_access_token), fontWeight = FontWeight.Bold)
        ClickableText(
            text = profileViewModel.accessToken,
            showPadding = false,
            style = MaterialTheme.typography.body1,
            onClick = { copyClipBoard(context, profileViewModel.accessToken) })
        Spacer(Modifier.height(dimensionResource(id = R.dimen.profile_item_spacer)))

        Text(text = stringResource(R.string.profile_menu_gamebase_push_token), fontWeight = FontWeight.Bold)
        ClickableText(
            text = profileViewModel.pushToken,
            showPadding = false,
            style = MaterialTheme.typography.body1,
            onClick = { copyClipBoard(context, profileViewModel.pushToken) })
        Spacer(Modifier.height(dimensionResource(id = R.dimen.profile_item_spacer)))

        Text(text = stringResource(R.string.profile_menu_last_logged_in_provider), fontWeight = FontWeight.Bold)
        Text(text = profileViewModel.lastLoggedInProvider, style = MaterialTheme.typography.body1)
        Spacer(Modifier.height(dimensionResource(id = R.dimen.profile_item_spacer)))

        Text(text = stringResource(R.string.profile_menu_connected_idp_list), fontWeight = FontWeight.Bold)
        for (idp in profileViewModel.connectedIdpList) {
            Text(text = idp, style = MaterialTheme.typography.body1)
        }
    }
}

@Preview(widthDp = 360, heightDp = 720)
@Composable
fun ProfilePreview() {
    ProfileScreen(LocalContext.current as Activity)
}