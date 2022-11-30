/*
 * © NHN Corp. All rights reserved.
 * NHN Corp. PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.toast.android.gamebase.sample.ui.settings.idpmap

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
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.toast.android.gamebase.base.GamebaseException
import com.toast.android.gamebase.sample.GamebaseActivity
import com.toast.android.gamebase.sample.R
import com.toast.android.gamebase.sample.data.getIconResourceById
import com.toast.android.gamebase.sample.data.supportedIdpList
import com.toast.android.gamebase.sample.ui.components.dialog.ConfirmAlertDialog
import com.toast.android.gamebase.sample.ui.components.button.ToggleButton
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
            isDialogOpened = viewModel.uiState != IdpMappingUiState.DEFAULT,
            title = stringResource(id = R.string.idp_mapping_dialog_title),
            description = getDialogDescription(
                activity as Context,
                currentSelectedItem.value,
                viewModel.uiState,
                viewModel.currentException),
            setDialogStatus = { newState ->
                if (!newState) {
                    viewModel.uiState = IdpMappingUiState.DEFAULT
                }
            }
        ){
            if (viewModel.uiState == IdpMappingUiState.SHOW_REMOVE_MAPPING_DIALOG) {
                viewModel.removeMapping(activity, currentSelectedItem.value)
            } else if (viewModel.uiState == IdpMappingUiState.SHOW_FORCE_MAPPING_DIALOG) {
                viewModel.forceMapping(activity, currentSelectedItem.value, viewModel.currentException)
            }
            viewModel.uiState = IdpMappingUiState.DEFAULT
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
        IdpMappingUiState.SHOW_FORCE_MAPPING_DIALOG -> {
            return context.resources.getString(R.string.idp_mapping_force_mapping_dialog_description)
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
        IdpMappingUiState.DEFAULT -> {
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
                vertical = dimensionResource(id = R.dimen.idp_mapping_column_padding))
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
