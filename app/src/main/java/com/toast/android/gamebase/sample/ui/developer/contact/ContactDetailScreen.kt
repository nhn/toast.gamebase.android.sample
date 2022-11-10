package com.toast.android.gamebase.sample.ui.developer.contact

import android.app.Activity
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ModifierInfo
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.toast.android.gamebase.sample.R
import com.toast.android.gamebase.sample.ui.common.InputDialog
import com.toast.android.gamebase.sample.ui.common.KeyValueInputDialog
import com.toast.android.gamebase.sample.ui.common.RoundButton
import com.toast.android.gamebase.sample.ui.common.TextFieldWithLabel
import com.toast.android.gamebase.sample.ui.developer.MenuItem

@Composable
fun ContactDetailScreen(
    activity: Activity = LocalContext.current as Activity,
    viewModel: ContactDetailViewModel = viewModel()) {

    Surface(color = MaterialTheme.colors.surface) {
        Box {
            Column(
                modifier = Modifier.padding(
                    dimensionResource(id = R.dimen.setting_screen_column_padding_horizontal)
                )
            ) {
                TextFieldWithLabel(
                    labelName = stringResource(id = R.string.developer_contact_user_name),
                    fieldEnabled = true,
                    fieldMessage = viewModel.userName.value,
                    onValueChanged = { textFieldValue ->
                        viewModel.userName.value = textFieldValue
                    }
                )

                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.setting_screen_list_item_text_padding)))
                TextFieldWithLabel(
                    labelName = stringResource(id = R.string.developer_contact_additional_url),
                    fieldEnabled = true,
                    fieldMessage = viewModel.additionalUrl.value,
                    onValueChanged = { textFieldValue ->
                        viewModel.additionalUrl.value = textFieldValue
                    }
                )

                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.setting_screen_list_item_text_padding)))
                Text(stringResource(id = R.string.developer_contact_additional_parameter))
                LazyColumnForMap(viewModel.additionalParameters) {
                    viewModel.isAdditionalParametersInputDialogOpened.value = true
                }

                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.setting_screen_list_item_text_padding)))
                Text(stringResource(id = R.string.developer_contact_extra_data))
                LazyColumnForMap(viewModel.extraData) {
                    viewModel.isExtraDataInputDialogOpened.value = true
                }

                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.setting_screen_list_item_text_padding)))
                RoundButton(buttonText =
                    stringResource(id = R.string.developer_contact_open_contact)) {
                    viewModel.openContactUrl(activity)
                }
            }
            KeyValueInputDialog(
                title = stringResource(R.string.developer_contact_additional_url),
                isDialogOpened = viewModel.isAdditionalParametersInputDialogOpened.value,
                setDialogStatus = { newState ->
                    viewModel.isAdditionalParametersInputDialogOpened.value = newState},
                onOkButtonClicked = { inputKey, inputValue ->
                    viewModel.saveAdditionalParameter(inputKey, inputValue)
                })
            KeyValueInputDialog(
                title = stringResource(R.string.developer_contact_extra_data),
                isDialogOpened = viewModel.isExtraDataInputDialogOpened.value,
                setDialogStatus = { newState ->
                    viewModel.isExtraDataInputDialogOpened.value = newState },
                onOkButtonClicked = { inputKey, inputValue ->
                    viewModel.saveExtraData(inputKey, inputValue)
                })
        }
    }
}

@Composable
fun LazyColumnForMap(map: Map<String, Any>, clickListener: () -> Unit = {}) {
    Box(modifier = Modifier
        .border(
            width = dimensionResource(id = R.dimen.contact_screen_lazy_colum_border_width),
            color = Color.Gray,
            shape = RoundedCornerShape(
                dimensionResource(id = R.dimen.contact_screen_lazy_colum_border_corner_radius)
            )
        )
        .clickable {
            clickListener()
        }
    ) {
        if (map.isEmpty()) {
            Row(modifier = Modifier
                .fillMaxWidth()
                .height(
                    dimensionResource(id = R.dimen.contact_screen_empty_list_min_height)
                ),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically) {
                Text(stringResource(id = R.string.empty_list_message))
            }
        } else {
            LazyColumn(
            ) {
                items(items = map.keys.toList()) { key ->
                    MapItem(key, map[key].toString())
                }
            }
        }
    }
}

@Composable
fun MapItem(key: String, value: String?) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(dimensionResource(id = R.dimen.setting_screen_list_item_vertical_padding)),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(text = key,
            color = MaterialTheme.colors.primary,
            modifier = Modifier.weight(1f))
        Spacer(modifier = Modifier.width(
            dimensionResource(id = R.dimen.contact_screen_lazy_colum_item_horizontal_spacer)
        ))
        Text(text = value ?: "",
            modifier = Modifier.weight(1f))
    }
}

@Preview
@Composable
fun PreviewLazyColumnForMap() {
    val testMap = mapOf<String, String>("a" to "A", "b" to "B", "c" to "C")
    LazyColumnForMap(testMap)
}

@Preview
@Composable
fun PreviewEmptyLazyColumnForMap() {
    val testMap = mapOf<String, String>()
    LazyColumnForMap(testMap)
}

@Preview
@Composable
fun PreviewMapItemLong() {
    val longString = "Lorem ipsum dolor sit amet, consectetur adipiscing elit."+
            "Mauris egestas, magna nec luctus pellentesque, turpis tellus pulvinar ipsum," +
            " scelerisque auctor magna purus nec odio."
    MapItem(longString, longString)
}

@Preview
@Composable
fun PreviewContactDetailScreen() {
    ContactDetailScreen()
}
