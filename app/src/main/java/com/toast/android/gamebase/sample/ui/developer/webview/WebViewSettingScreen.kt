package com.toast.android.gamebase.sample.ui.developer.webview

import android.app.Activity
import android.graphics.Color
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.toast.android.gamebase.sample.R
import com.toast.android.gamebase.sample.ui.components.text.ClickableText
import com.toast.android.gamebase.sample.ui.components.input.DropdownMenuBoxWithTitle
import com.toast.android.gamebase.sample.ui.components.button.RoundButton
import com.toast.android.gamebase.sample.ui.components.dialog.ConfirmAlertDialog
import com.toast.android.gamebase.sample.ui.components.input.SwitchWithLabel
import com.toast.android.gamebase.sample.ui.developer.WEBVIEW_MENU_DEFAULT_URL

@Composable
fun WebViewSettingScreen(
    activity: Activity = LocalContext.current as Activity,
    viewModel: WebViewSettingViewModel = viewModel()
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(dimensionResource(id = R.dimen.webview_setting_column_padding))
            .verticalScroll(scrollState)
    ) {
        SwitchWithLabel(
            label = stringResource(id = R.string.developer_web_view_configuration_navigation_bar_visible),
            state = viewModel.navigationBarVisibleStatus.value,
            enableSwitch = true,
            event = { newState ->
                viewModel.navigationBarVisibleStatus.value = newState
            })
        SwitchWithLabel(
            label = stringResource(id = R.string.developer_web_view_configuration_navigation_bar_back_button),
            state = viewModel.navigationBarBackButtonStatus.value,
            enableSwitch = true,
            event = { newState ->
                viewModel.navigationBarBackButtonStatus.value = newState
            })
        SwitchWithLabel(
            label = stringResource(id = R.string.developer_web_view_configuration_navigation_display_cutout),
            state = viewModel.renderOutSafeArea.value,
            enableSwitch = true,
            event = { newState ->
                viewModel.renderOutSafeArea.value = newState
            })
        ClickableText(stringId = R.string.developer_web_view_configuration_navigation_bar_title) {
            viewModel.navigationBarTitleDialogStatus.value = true
        }
        ClickableText(stringId = R.string.developer_web_view_configuration_navigation_bar_color) {
            viewModel.navigationBarColorDialogStatus.value = true
        }
        ClickableText(stringId = R.string.developer_web_view_configuration_navigation_bar_height) {
            viewModel.navigationBarHeightDialogStatus.value = true
        }
        DropdownMenuBoxWithTitle(
            title = stringResource(id = R.string.developer_web_view_configuration_navigation_bar_orientation),
            options = stringArrayResource(id = R.array.screen_orientation).toList(),
            expanded = viewModel.screenOrientationExpanded.value,
            onExpandChanged = { expand ->
                viewModel.screenOrientationExpanded.value = expand
            },
            selected = viewModel.screenOrientationType.value,
            onSelected = { selectedId ->
                viewModel.screenOrientationType.value = selectedId
            },
            modifier = Modifier.width(dimensionResource(id = R.dimen.webview_setting_drop_down_width))
        )
        RoundButton(buttonText = stringResource(id = R.string.developer_web_view_show)) {
            viewModel.openWebViewDialogStatus.value = true
        }
    }

    WebViewSettingDialog(
        isDialogOpened = viewModel.navigationBarTitleDialogStatus.value,
        title = stringResource(id = R.string.developer_web_view_configuration_navigation_bar_title),
        labelName = stringResource(id = R.string.developer_web_view_configuration_navigation_bar_title_label_name),
        message = viewModel.navigationBarTitle.value,
        setDialogStatus = { newState ->
            viewModel.navigationBarTitleDialogStatus.value = newState
        },
        onOkButtonClicked = { value ->
            viewModel.navigationBarTitle.value = value
        }
    )

    WebViewSettingDialog(
        isDialogOpened = viewModel.navigationBarColorDialogStatus.value,
        title = stringResource(id = R.string.developer_web_view_configuration_navigation_bar_color),
        labelName = stringResource(id = R.string.developer_web_view_configuration_navigation_bar_color_label_name),
        message = viewModel.navigationBarColor.value,
        setDialogStatus = { newState ->
            viewModel.navigationBarColorDialogStatus.value = newState
        },
        onOkButtonClicked = { value ->
            try {
                Color.parseColor(value)
            } catch (e: IllegalArgumentException) {
                viewModel.openColorInputInvalidAlertStatus.value = true
                return@WebViewSettingDialog
            }
            viewModel.navigationBarColor.value = value
        }
    )

    ConfirmAlertDialog(
        isDialogOpened = viewModel.openColorInputInvalidAlertStatus.value,
        title = "Failed",
        description = "Color is invalid",
        onOkButtonClicked = {},
        setDialogStatus = { newState ->
            viewModel.openColorInputInvalidAlertStatus.value = newState
        }
    )

    WebViewSettingDialog(
        isDialogOpened = viewModel.navigationBarHeightDialogStatus.value,
        title = stringResource(id = R.string.developer_web_view_configuration_navigation_bar_height),
        labelName = stringResource(id = R.string.developer_web_view_configuration_navigation_bar_height_label_name),
        message = viewModel.navigationBarHeight.value.toString(),
        setDialogStatus = { newState ->
            viewModel.navigationBarHeightDialogStatus.value = newState
        },
        onOkButtonClicked = { value ->
            viewModel.navigationBarHeight.value = value.toInt()
        }
    )

    OpenCustomWebViewDialog(
        isDialogOpened = viewModel.openWebViewDialogStatus.value,
        title = stringResource(R.string.developer_web_view_show),
        fieldMessage = WEBVIEW_MENU_DEFAULT_URL,
        setDialogStatus = { newState ->
            viewModel.openWebViewDialogStatus.value = newState
        },
        onOkButtonClicked = { value ->
            viewModel.openWebView(activity = activity, urlString = value)
        }
    )
}