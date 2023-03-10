package com.toast.android.gamebase.sample.ui.settings.idpmap

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.toast.android.gamebase.base.GamebaseException
import com.toast.android.gamebase.base.auth.AuthProvider
import com.toast.android.gamebase.sample.GamebaseActivity
import com.toast.android.gamebase.sample.R
import com.toast.android.gamebase.sample.data.getIconResourceById
import com.toast.android.gamebase.sample.data.lineRegionList
import com.toast.android.gamebase.sample.data.supportedIdpList
import com.toast.android.gamebase.sample.gamebase_manager.getUserID
import com.toast.android.gamebase.sample.ui.components.dialog.ConfirmAlertDialog
import com.toast.android.gamebase.sample.ui.components.button.ToggleButton
import com.toast.android.gamebase.sample.ui.components.dialog.DropDownMenuDialog
import com.toast.android.gamebase.sample.ui.theme.White

@Composable
fun IdpMappingScreen(
    activity: GamebaseActivity,
    viewModel: IdpMappingViewModel = viewModel()
) {
    val currentSelectedItem = remember { mutableStateOf(supportedIdpList[0]) }
    Box {
        MappingColumn(
            activity,
            viewModel
        ) {
            currentSelectedItem.value = it
        }
    }
    if (viewModel.uiState == IdpMappingUiState.SHOW_FORCE_MAPPING_INFO_DIALOG) {
        ForceMappingInfoDialog(
            currentIdp = currentSelectedItem.value,
            mappedUserId = viewModel.forcingMappingTicket?.mappedUserId?: "",
            onForceMapping = {
                viewModel.uiState = IdpMappingUiState.SHOW_FORCE_MAPPING_CONFIRM_DIALOG
            },
            onChangeLogin = {
                viewModel.uiState = IdpMappingUiState.DEFAULT
                viewModel.changeLogin(activity)
            },
            onCancel = {
                viewModel.uiState = IdpMappingUiState.DEFAULT
            }
        )
    }

    ConfirmAlertDialog(
        isDialogOpened = viewModel.uiState != IdpMappingUiState.DEFAULT && viewModel.uiState != IdpMappingUiState.SHOW_FORCE_MAPPING_INFO_DIALOG,
        title = stringResource(id = R.string.idp_mapping_dialog_title),
        description = getDialogDescription(
            activity as Context,
            currentSelectedItem.value,
            viewModel.uiState,
            viewModel.currentException
        ),
        setDialogStatus = { newState ->
            if (!newState) {
                viewModel.uiState = IdpMappingUiState.DEFAULT
            }
        },
        showCancel = true
    ) {
        if (viewModel.uiState == IdpMappingUiState.SHOW_REMOVE_MAPPING_DIALOG) {
            viewModel.removeMapping(activity, currentSelectedItem.value)
        } else if (viewModel.uiState == IdpMappingUiState.SHOW_FORCE_MAPPING_CONFIRM_DIALOG) {
            viewModel.forceMapping(activity, currentSelectedItem.value)
        }
        viewModel.uiState = IdpMappingUiState.DEFAULT
    }

    DropDownMenuDialog(
        title = stringResource(id = R.string.login_select_line_region),
        isDialogOpened = viewModel.requiredAdditionalInfo,
        setDialogStatus = { newState ->
            viewModel.requiredAdditionalInfo = newState
        },
        options = lineRegionList,
        onOkButtonClicked = { selected ->
            viewModel.enteredRegion = lineRegionList[selected]
            viewModel.addMapping(activity, AuthProvider.LINE)
        }
    )
}

@Composable
private fun ForceMappingInfoDialog(
    currentIdp: String,
    mappedUserId: String,
    onForceMapping: () -> Unit,
    onChangeLogin: () -> Unit,
    onCancel: () -> Unit
) {
    Dialog(onDismissRequest = { }) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(80f)
                .wrapContentHeight(),
            shape = RoundedCornerShape(
                dimensionResource(id = R.dimen.drop_down_menu_box_dialog_round_corner_radius)
            ),
            color = MaterialTheme.colors.surface
        ) {
            Column(
                modifier = Modifier.padding(dimensionResource(id = R.dimen.drop_down_menu_box_dialog_column_padding)),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            bottom =
                            dimensionResource(id = R.dimen.drop_down_menu_box_dialog_title_padding_bottom)
                        ),
                    text = stringResource(id = R.string.idp_mapping_already_mapped, currentIdp, mappedUserId),
                    textAlign = TextAlign.Left
                )
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    TextButton(
                        onClick = onForceMapping
                    ) {
                        Text(stringResource(id = R.string.idp_mapping_guide_force_mapping))
                    }
                    TextButton(
                        onClick = onChangeLogin
                    ) {
                        Text(stringResource(id = R.string.idp_mapping_guide_change_login))
                    }
                    TextButton(
                        onClick = onCancel
                    ) {
                        Text(stringResource(id = R.string.button_cancel))
                    }
                }
            }
        }
    }
}

private fun getDialogDescription(
    context: Context,
    currentSelectedItem: String,
    uiState: IdpMappingUiState,
    currentException: GamebaseException?
): String {
    when (uiState) {
        IdpMappingUiState.SHOW_REMOVE_MAPPING_DIALOG -> {
            return currentSelectedItem + " " + context.resources.getString(R.string.idp_mapping_dialog_description)
        }
        IdpMappingUiState.SHOW_FORCE_MAPPING_CONFIRM_DIALOG -> {
            return context.resources.getString(R.string.idp_mapping_force_mapping_confirm_dialog_description)
        }
        IdpMappingUiState.MAPPING_FAILED -> {
            return context.resources.getString(R.string.idp_mapping_failed_dialog_description) +
                    "\n${currentException?.message}"
        }
        IdpMappingUiState.REMOVE_MAPPING_FAILED -> {
            return context.resources.getString(R.string.idp_mapping_remove_mapping_failed_dialog_description) +
                    "\n${currentException?.message}"
        }
        IdpMappingUiState.FORCE_MAPPING_FAILED -> {
            return context.resources.getString(R.string.idp_mapping_force_mapping_failed_dialog_description) +
                    "\n${currentException?.message}"
        }
        IdpMappingUiState.CHANGE_LOGIN_FAILED -> {
            return context.resources.getString(R.string.idp_mapping_dialog_description)
        }
        IdpMappingUiState.DEFAULT -> {
            return ""
        }
        IdpMappingUiState.SHOW_FORCE_MAPPING_INFO_DIALOG -> {
            // No description in force mapping info dialog
            return ""
        }
    }
}

@Composable
fun MappingColumn(
    activity: GamebaseActivity,
    viewModel: IdpMappingViewModel,
    setCurrentSelectedItem: (String) -> Unit,
) {
    LaunchedEffect(Unit) {
        viewModel.fetchAuthMappingList()
    }
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(White)
    ) {
        LazyColumn(
            contentPadding = PaddingValues(
                horizontal = dimensionResource(id = R.dimen.idp_mapping_column_padding),
                vertical = dimensionResource(id = R.dimen.idp_mapping_column_padding)
            )
        ) {
            items(
                items = supportedIdpList.drop(1),
                key = { item ->
                    item
                }) { idp ->
                ListItem(activity, idp, viewModel) {
                    setCurrentSelectedItem(idp)
                }
            }
        }
    }
}

@Composable
fun ListItem(
    activity: GamebaseActivity,
    idp: String,
    viewModel: IdpMappingViewModel,
    setCurrentSelectedItem: () -> Unit
) {
    Box {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = dimensionResource(id = R.dimen.idp_mapping_item_row_padding_vertical),
                    bottom = dimensionResource(id = R.dimen.idp_mapping_item_row_padding_vertical),
                    start = dimensionResource(id = R.dimen.idp_mapping_item_row_padding_horizontal),
                    end = dimensionResource(id = R.dimen.idp_mapping_item_row_padding_horizontal)
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {

            Image(
                painter = painterResource(getIconResourceById(idp)),
                contentDescription = idp,
                modifier = Modifier
                    .width(dimensionResource(id = R.dimen.idp_mapping_image_size))
                    .height(dimensionResource(id = R.dimen.idp_mapping_image_size))
                    .padding(dimensionResource(id = R.dimen.idp_mapping_image_padding))
            )
            Spacer(Modifier.width(dimensionResource(id = R.dimen.idp_mapping_image_text_spacer)))
            Text(
                color = Color.Black,
                text = idp,
                textAlign = TextAlign.Center
            )
            Spacer(
                Modifier
                    .weight(1f)
                    .fillMaxHeight()
            )
            ToggleButton(
                state = viewModel.idpMappedMap.getOrDefault(idp, false),
                text1 = stringResource(id = R.string.idp_mapping_button_do_mapping),
                text2 = stringResource(id = R.string.idp_mapping_button_mapped)
            ) {
                setCurrentSelectedItem()
                if (viewModel.idpMappedMap[idp] != true) {
                    if (idp == AuthProvider.LINE) {
                        viewModel.requiredAdditionalInfo = true
                    } else {
                        viewModel.addMapping(activity, idp)
                    }
                } else {
                    viewModel.showRemoveMappingDialog()
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewForceMappingInfoDialog() {
    ForceMappingInfoDialog("Google", "sampleId", {}, {}, {})
}