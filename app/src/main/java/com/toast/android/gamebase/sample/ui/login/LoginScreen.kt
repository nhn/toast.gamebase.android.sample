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
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.toast.android.gamebase.base.auth.AuthProvider
import com.toast.android.gamebase.sample.GamebaseActivity
import com.toast.android.gamebase.sample.R
import com.toast.android.gamebase.sample.data.getIconResourceById
import com.toast.android.gamebase.sample.data.lineRegionList
import com.toast.android.gamebase.sample.data.supportedIdpList
import com.toast.android.gamebase.sample.gamebase_manager.isLoggedIn
import com.toast.android.gamebase.sample.gamebase_manager.openContact
import com.toast.android.gamebase.sample.ui.components.dialog.DropDownMenuDialog
import com.toast.android.gamebase.sample.ui.components.text.CopyrightFooter
import com.toast.android.gamebase.sample.ui.theme.GamebaseSampleProjectTheme

@Composable
fun LoginScreen(
    activity: GamebaseActivity,
    loginViewModel: LoginViewModel = viewModel(),
    onLoggedIn: () -> Unit,
) {
    LaunchedEffect(Unit) {
        if (isLoggedIn()) {
            onLoggedIn()
            return@LaunchedEffect
        }
        loginViewModel.tryLastIdpLogin(activity)
    }

    LaunchedEffect(loginViewModel.uiState) {
        if (loginViewModel.uiState == LoginState.LOGGED_IN) {
            onLoggedIn()
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
                    Spacer(modifier = Modifier.height(
                        dimensionResource(id = R.dimen.login_screen_margin_title)))
                    Text(
                        style = MaterialTheme.typography.h4,
                        text = stringResource(R.string.login_title)
                    )
                    Spacer(modifier = Modifier.height(
                        dimensionResource(id = R.dimen.login_screen_button_space)
                    ))
                    Text(stringResource(R.string.login_description))
                    Spacer(modifier = Modifier.height(
                        dimensionResource(id = R.dimen.login_screen_margin_title)))
                }
                itemsIndexed(supportedIdpList) { _, idp ->
                    OutlineLoginButton(
                        idp,
                        showLineRegionDialog = { loginViewModel.showRegionSelectDialog() },
                        doLogin = { loginViewModel.login(activity, idp) }
                    )
                    Spacer(modifier = Modifier.height(
                        dimensionResource(id = R.dimen.login_screen_icon_padding)
                    ))
                }
                item {
                    ContactTextButton { openContact(activity, null) {} }
                    CopyrightFooter()
                }
            }

            DropDownMenuDialog(
                title = stringResource(id = R.string.login_select_line_region),
                isDialogOpened = loginViewModel.uiState == LoginState.SHOW_LINE_REGION_DIALOG,
                setDialogStatus = { isDialogOpened ->
                    loginViewModel.setRegionDialogState(isDialogOpened)
                },
                options = lineRegionList,
                onOkButtonClicked = { selected ->
                    loginViewModel.onRegionDialogOkButtonClicked(activity, selected)
                }
            )
        }
    }
}

@Composable
fun ContactTextButton(openContact: () -> Unit) {
    TextButton(
        onClick = { openContact() }
    ) {
        Spacer(modifier = Modifier.width(
            dimensionResource(id = R.dimen.login_screen_contact_button_horizontal_space)
        ))
        Text(
            text = stringResource(id = R.string.developer_contact_open_contact),
            style = MaterialTheme.typography.caption
        )
        Spacer(modifier = Modifier.width(
            dimensionResource(id = R.dimen.login_screen_contact_button_horizontal_space)
        ))
    }
}

@Composable
fun OutlineLoginButton(
    idp: String,
    doLogin: () -> Unit,
    showLineRegionDialog: () -> Unit
) {
    OutlinedButton(
        modifier = Modifier
            .fillMaxWidth(0.9f),
        colors = ButtonDefaults.buttonColors(
            contentColor = Color.Black,
            backgroundColor = Color.White
        ),
        onClick = {
            if (idp == AuthProvider.LINE) {
                showLineRegionDialog()
            } else {
                doLogin()
            }
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
                    .width(dimensionResource(id = R.dimen.login_screen_icon_size))
                    .height(dimensionResource(id = R.dimen.login_screen_icon_size))
                    .padding(dimensionResource(id = R.dimen.login_screen_icon_padding))
            )
            Spacer(Modifier.width(dimensionResource(id = R.dimen.login_screen_icon_text_space)))
            Text(
                color = Color.Black,
                text = stringResource(id = R.string.sign_in_button, idp.capitalize()),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview
@Composable
fun PreviewLoginButton() {
    OutlineLoginButton("line", {}, {})
}
