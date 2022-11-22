package com.toast.android.gamebase.sample.ui.developer.push

import android.app.Activity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.toast.android.gamebase.sample.R
import com.toast.android.gamebase.sample.ui.components.input.DropdownMenuBox
import com.toast.android.gamebase.sample.ui.components.text.SubMenuDivider
import com.toast.android.gamebase.sample.ui.components.input.SwitchWithLabel
import com.toast.android.gamebase.sample.ui.components.button.RoundButton

@Composable
fun PushSettingScreen(
    viewModel: PushSettingViewModel = viewModel()
) {
    val scrollState = rememberScrollState()
    val activity: Activity = LocalContext.current as Activity
    var expandState by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.initialFetch(activity)
    }

    Column(modifier = Modifier
        .padding(20.dp)
        .verticalScroll(scrollState)) {

        SubMenuDivider(R.string.developer_push_configuration_sub_title)
        SwitchWithLabel(
            stringId = R.string.setting_normal_push_title,
            state = viewModel.enablePush.value,
            enableSwitch = true,
            event = { newState ->
                viewModel.enablePush.value = newState
                if (!viewModel.enablePush.value) {
                    viewModel.enableAdAgreement.value = false
                    viewModel.enableAdAgreementNight.value = false
                }
            }
        )
        SwitchWithLabel(
            stringId = R.string.setting_advertising_push_title,
            state = viewModel.enableAdAgreement.value,
            enableSwitch = viewModel.enablePush.value, // depends on enable push
            event = { newState ->
                viewModel.enableAdAgreement.value = newState
                if (!viewModel.enableAdAgreement.value) {
                    viewModel.enableAdAgreementNight.value = false
                }
            }
        )
        SwitchWithLabel(
            stringId = R.string.setting_night_advertising_push_title,
            state = viewModel.enableAdAgreementNight.value,
            enableSwitch = viewModel.enablePush.value && viewModel.enableAdAgreement.value,
            event = { newState ->
                viewModel.enableAdAgreementNight.value = newState
            }
        )

        SubMenuDivider(R.string.developer_push_notification_options_sub_title)
        SwitchWithLabel(
            stringId = R.string.setting_push_foreground_title,
            state = viewModel.enableForeground.value,
            enableSwitch = true,
            event = { newState ->
                viewModel.enableForeground.value = newState
            }
        )
        SwitchWithLabel(
            stringId = R.string.developer_push_noti_enable_badge,
            state = viewModel.enableBadge.value,
            enableSwitch = true,
            event = { newState ->
                viewModel.enableBadge.value = newState
            }
        )
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = stringResource(id = R.string.developer_push_noti_enable_set_priority))
            DropdownMenuBox(
                modifier = Modifier
                    .width(150.dp)
                    .height(50.dp),
                options = viewModel.noticePriorityOptions,
                expanded = expandState,
                onExpandChanged = { expand -> expandState = expand },
                selected = viewModel.notificationPriority.value,
                onSelected = { selectedId ->
                    viewModel.notificationPriority.value = selectedId
                }
            )
        }
        RoundButton(buttonText = stringResource(id = R.string.button_ok)) {
            viewModel.updatePushNotificationOptions(activity)
        }
    }
}
