package com.toast.android.gamebase.sample.ui.developer.analytics

import android.app.Activity
import androidx.compose.foundation.layout.*
import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.toast.android.gamebase.sample.R
import com.toast.android.gamebase.sample.ui.components.input.TextFieldWithLabel

@Composable
fun SetGameUserDataDialog(
    activity: Activity,
    isDialogOpened: Boolean,
    title: String,
    setDialogStatus: (Boolean) -> Unit
) {
    if (isDialogOpened) {
        val setGameUserDataDialogStateHolder = SetGameUserDataDialogStateHolder()

        AlertDialog(modifier = Modifier.fillMaxWidth(), onDismissRequest = {
            setDialogStatus(false)
        },
            title = {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 50.dp),
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
                    Spacer(modifier = Modifier.height(5.dp))
                }
            },
            buttons = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    TextButton(
                        onClick = {
                            setGameUserDataDialogStateHolder.setGameUserDataInDialog(activity)
                            setDialogStatus(false)
                        }
                    ) {
                        Text(stringResource(id = R.string.button_ok))
                    }
                    TextButton(
                        onClick = {
                            setDialogStatus(false)
                        }
                    ) {
                        Text(stringResource(id = R.string.button_cancel))
                    }
                }
            }
        )
    }
}