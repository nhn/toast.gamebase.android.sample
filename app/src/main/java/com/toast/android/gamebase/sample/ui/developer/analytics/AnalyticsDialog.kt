package com.toast.android.gamebase.sample.ui.developer.analytics

import android.app.Activity
import androidx.compose.foundation.layout.*
import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.toast.android.gamebase.sample.R
import com.toast.android.gamebase.sample.ui.components.input.TextFieldWithLabel

@Composable
fun SetGameUserDataDialog(
    activity: Activity,
    isDialogOpened: Boolean,
    title: String,
    setDialogState: (Boolean) -> Unit
) {
    if (isDialogOpened) {
        val setGameUserDataDialogStateHolder = SetGameUserDataDialogStateHolder()

        AlertDialog(modifier = Modifier.fillMaxWidth(), onDismissRequest = {
            setDialogState(false)
        },
            title = {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            bottom =
                            dimensionResource(id = R.dimen.analytics_dialog_title_bottom_padding)
                        ),
                    text = title,
                    textAlign = TextAlign.Center
                )
            },
            text = {
                Column(modifier = Modifier.fillMaxWidth()) {
                    TextFieldWithLabel(
                        labelName = stringResource(id = R.string.developer_analytics_level_label_name),
                        fieldMessage = setGameUserDataDialogStateHolder.levelInput.value,
                        onValueChanged = { value ->
                            setGameUserDataDialogStateHolder.levelInput.value = value
                        }
                    )
                    TextFieldWithLabel(
                        labelName = stringResource(id = R.string.developer_analytics_channel_id_label_name),
                        fieldMessage = setGameUserDataDialogStateHolder.channelId.value,
                        onValueChanged = { value ->
                            setGameUserDataDialogStateHolder.channelId.value = value
                        }
                    )
                    TextFieldWithLabel(
                        labelName = stringResource(id = R.string.developer_analytics_character_id_label_name),
                        fieldMessage = setGameUserDataDialogStateHolder.characterId.value,
                        onValueChanged = { value ->
                            setGameUserDataDialogStateHolder.characterId.value = value
                        }
                    )
                    TextFieldWithLabel(
                        labelName = stringResource(id = R.string.developer_analytics_class_id_label_name),
                        fieldMessage = setGameUserDataDialogStateHolder.classId.value,
                        onValueChanged = { value ->
                            setGameUserDataDialogStateHolder.classId.value = value
                        }
                    )
                    Spacer(modifier = Modifier.height(
                        dimensionResource(id = R.dimen.analytics_dialog_text_bottom_space)
                    ))
                }
            },
            buttons = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom =
                        dimensionResource(id = R.dimen.analytics_dialog_button_row_padding_bottom)),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    TextButton(
                        onClick = {
                            setGameUserDataDialogStateHolder.setGameUserDataInDialog(activity)
                            setDialogState(false)
                        }
                    ) {
                        Text(stringResource(id = R.string.button_ok))
                    }
                    TextButton(
                        onClick = {
                            setDialogState(false)
                        }
                    ) {
                        Text(stringResource(id = R.string.button_cancel))
                    }
                }
            }
        )
    }
}