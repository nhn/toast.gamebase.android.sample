package com.toast.android.gamebase.sample.ui.idpmap

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.toast.android.gamebase.base.GamebaseException
import com.toast.android.gamebase.sample.GamebaseActivity
import com.toast.android.gamebase.sample.R
import com.toast.android.gamebase.sample.getIconResourceById
import com.toast.android.gamebase.sample.supportedIdpList
import com.toast.android.gamebase.sample.ui.common.ConfirmAlertDialog
import com.toast.android.gamebase.sample.ui.common.ToggleButton
import com.toast.android.gamebase.sample.ui.theme.White

@Composable
fun IdpMappingScreen(
    activity: GamebaseActivity,
    viewModel: IdpMappingViewModel = viewModel()
) {
    val currentSelectedItem = remember { mutableStateOf(supportedIdpList[0])}
    Box {
        MappingColumn(
            activity,
            viewModel
        ){
            currentSelectedItem.value = it
        }
        ConfirmAlertDialog(
            dialogOpened = viewModel.uiState != IDP_MAPPING_UI_STATE.DEFAULT,
            title = stringResource(id = R.string.idp_mapping_dialog_title),
            description = getDialogDescription(
                activity as Context,
                currentSelectedItem.value,
                viewModel.uiState,
                viewModel.currentException),
            setDialogStatus = { newState ->
                if (!newState) {
                    viewModel.uiState = IDP_MAPPING_UI_STATE.DEFAULT
                }
            }
        ){
            if (viewModel.uiState == IDP_MAPPING_UI_STATE.SHOW_REMOVE_MAPPING_DIALOG) {
                viewModel.removeMapping(activity, currentSelectedItem.value)
            } else if (viewModel.uiState == IDP_MAPPING_UI_STATE.SHOW_FORCE_MAPPING_DIALOG) {
                viewModel.forceMapping(activity, currentSelectedItem.value, viewModel.currentException)
            }
            viewModel.uiState = IDP_MAPPING_UI_STATE.DEFAULT
        }
    }
}

private fun getDialogDescription(
    context: Context,
    currentSelectedItem: String,
    uiState: IDP_MAPPING_UI_STATE,
    currentException: GamebaseException?
): String {
    when (uiState) {
        IDP_MAPPING_UI_STATE.SHOW_REMOVE_MAPPING_DIALOG -> {
            return currentSelectedItem + " " + context.resources.getString(R.string.idp_mapping_dialog_description)
        }
        IDP_MAPPING_UI_STATE.SHOW_FORCE_MAPPING_DIALOG -> {
            return context.resources.getString(R.string.idp_mapping_force_mapping_dialog_description)
        }
        IDP_MAPPING_UI_STATE.MAPPING_FAILED -> {
            return context.resources.getString(R.string.idp_mapping_failed_dialog_description) +
                    "\n${currentException?.message}"
        }
        IDP_MAPPING_UI_STATE.REMOVE_MAPPING_FAILED -> {
            return context.resources.getString(R.string.idp_mapping_remove_mapping_failed_dialog_description) +
                    "\n${currentException?.message}"
        }
        IDP_MAPPING_UI_STATE.FORCE_MAPPING_FAILED -> {
            return context.resources.getString(R.string.idp_mapping_force_mapping_failed_dialog_description) +
            "\n${currentException?.message}"
        }
        IDP_MAPPING_UI_STATE.DEFAULT -> {
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
            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp)
        ) {
            items (
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
                .padding(top = 10.dp, bottom = 10.dp, start = 20.dp, end = 20.dp),
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
                text = idp,
                textAlign = TextAlign.Center
            )
            Spacer(
                Modifier
                    .weight(1f)
                    .fillMaxHeight())
            ToggleButton(
                state = viewModel.idpMappedMap.getOrDefault(idp, false),
                text1 = stringResource(id = R.string.idp_mapping_button_do_mapping),
                text2 = stringResource(id = R.string.idp_mapping_button_mapped)
            ) {
                setCurrentSelectedItem()
                if (viewModel.idpMappedMap[idp] != true) {
                    viewModel.addMapping(activity, idp)
                } else {
                    viewModel.showRemoveMappingDialog()
                }
            }
        }
    }
}
