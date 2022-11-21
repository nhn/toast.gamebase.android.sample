package com.toast.android.gamebase.sample.ui.contact

import android.app.Activity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.toast.android.gamebase.sample.R
import com.toast.android.gamebase.sample.ui.components.KeyValueInputDialog
import com.toast.android.gamebase.sample.ui.components.RoundButton
import com.toast.android.gamebase.sample.ui.components.TextFieldWithLabel
import com.toast.android.gamebase.sample.ui.theme.TextFieldColor
import com.toast.android.gamebase.sample.ui.theme.Toast

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
                    fieldMessage = viewModel.userName.value,
                    onValueChanged = { textFieldValue ->
                        viewModel.userName.value = textFieldValue
                    }
                )

                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.setting_screen_list_item_text_padding)))
                TextFieldWithLabel(
                    labelName = stringResource(id = R.string.developer_contact_additional_url),
                    fieldMessage = viewModel.additionalUrl.value,
                    onValueChanged = { textFieldValue ->
                        viewModel.additionalUrl.value = textFieldValue
                    }
                )

                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.setting_screen_list_item_text_padding)))
                Text(
                    text = stringResource(id = R.string.developer_contact_additional_parameter),
                    style = TextStyle(color = Toast)
                )
                LazyColumnForMap(
                    viewModel.additionalParameters,
                    clickListener = {
                        viewModel.isAdditionalParametersInputDialogOpened.value = true
                    },
                    onItemRemoveClicked = {
                        viewModel.removeAdditionalParameter(it)
                    }
                )

                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.setting_screen_list_item_text_padding)))
                Text(
                    text = stringResource(id = R.string.developer_contact_extra_data),
                    style = TextStyle(color = Toast)
                )
                LazyColumnForMap(
                    viewModel.extraData,
                    clickListener = {
                        viewModel.isExtraDataInputDialogOpened.value = true
                    },
                    onItemRemoveClicked = {
                        viewModel.removeExtraData(it)
                    }
                )

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
fun LazyColumnForMap(
    map: Map<String, Any>,
    clickListener: () -> Unit,
    onItemRemoveClicked: (String) -> Unit)
{
    Box(modifier = Modifier
        .clip(
            RoundedCornerShape(
                dimensionResource(id = R.dimen.contact_screen_lazy_column_border_corner_radius)
            )
        )
        .clickable {
            clickListener()
        }
    ) {
        Surface(color = TextFieldColor) {
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
                        MapItem(key, map[key].toString()) {
                            onItemRemoveClicked(key)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MapItem(key: String, value: String?, onRemoveClicked: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = dimensionResource(id = R.dimen.contact_screen_empty_list_min_height))
            .padding(dimensionResource(id = R.dimen.setting_screen_list_item_vertical_padding)),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(text = key,
            color = MaterialTheme.colors.primary,
            modifier = Modifier.weight(1f))
        Spacer(modifier = Modifier.width(
            dimensionResource(id = R.dimen.contact_screen_lazy_column_item_horizontal_spacer)
        ))
        Text(text = value ?: "",
            modifier = Modifier.weight(1f))
        IconButton(onClick = onRemoveClicked) {
            Icon(
                Icons.Filled.Close,
                contentDescription = stringResource(
                        id = R.string.developer_contact_remove_item_button_content_description)
            )
        }
    }
}

@Preview
@Composable
fun PreviewLazyColumnForMap() {
    val testMap = mapOf<String, String>("a" to "A", "b" to "B", "c" to "C")
    LazyColumnForMap(testMap, {}, {})
}

@Preview
@Composable
fun PreviewEmptyLazyColumnForMap() {
    val testMap = mapOf<String, String>()
    LazyColumnForMap(testMap, {}, {})
}

@Preview
@Composable
fun PreviewMapItemLong() {
    val longString = "Lorem ipsum dolor sit amet, consectetur adipiscing elit."+
            "Mauris egestas, magna nec luctus pellentesque, turpis tellus pulvinar ipsum," +
            " scelerisque auctor magna purus nec odio."
    MapItem(longString, longString) {}
}

@Preview
@Composable
fun PreviewContactDetailScreen() {
    ContactDetailScreen()
}
