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
            state = viewModel.navigationBarVisibleStatus,
            enableSwitch = true,
            event = { newState ->
                viewModel.navigationBarVisibleStatus = newState
            })
        SwitchWithLabel(
            label = stringResource(id = R.string.developer_web_view_configuration_navigation_bar_back_button),
            state = viewModel.navigationBarBackButtonStatus,
            enableSwitch = true,
            event = { newState ->
                viewModel.navigationBarBackButtonStatus = newState
            })
        SwitchWithLabel(
            label = stringResource(id = R.string.developer_web_view_configuration_navigation_display_cutout),
            state = viewModel.renderOutSafeArea,
            enableSwitch = true,
            event = { newState ->
                viewModel.renderOutSafeArea = newState
            })
        ClickableText(stringId = R.string.developer_web_view_configuration_navigation_bar_title) {
            viewModel.navigationBarTitleDialogStatus = true
        }
        ClickableText(stringId = R.string.developer_web_view_configuration_navigation_bar_color) {
            viewModel.navigationBarColorDialogStatus = true
        }
        ClickableText(stringId = R.string.developer_web_view_configuration_navigation_bar_height) {
            viewModel.navigationBarHeightDialogStatus = true
        }
        ClickableText(stringId = R.string.developer_web_view_configuration_cutout_color) {
            viewModel.cutoutAreaColorDialogStatus = true
        }
        DropdownMenuBoxWithTitle(
            title = stringResource(id = R.string.developer_web_view_configuration_navigation_bar_orientation),
            options = stringArrayResource(id = R.array.screen_orientation).toList(),
            expanded = viewModel.screenOrientationExpanded,
            onExpandChanged = { expand ->
                viewModel.screenOrientationExpanded = expand
            },
            selected = viewModel.screenOrientationType,
            onSelected = { selectedId ->
                viewModel.screenOrientationType = selectedId
            },
            modifier = Modifier.width(dimensionResource(id = R.dimen.webview_setting_drop_down_width))
        )
        RoundButton(buttonText = stringResource(id = R.string.developer_web_view_show)) {
            viewModel.openWebViewDialogStatus = true
        }
    }

    WebViewSettingDialog(
        isDialogOpened = viewModel.navigationBarTitleDialogStatus,
        title = stringResource(id = R.string.developer_web_view_configuration_navigation_bar_title),
        labelName = stringResource(id = R.string.developer_web_view_configuration_navigation_bar_title_label_name),
        message = viewModel.navigationBarTitle,
        setDialogStatus = { newState ->
            viewModel.navigationBarTitleDialogStatus = newState
        },
        onOkButtonClicked = { text ->
            viewModel.navigationBarTitle = text
        }
    )

    WebViewSettingDialog(
        isDialogOpened = viewModel.navigationBarColorDialogStatus,
        title = stringResource(id = R.string.developer_web_view_configuration_navigation_bar_color),
        labelName = stringResource(id = R.string.developer_web_view_configuration_navigation_bar_color_label_name),
        message = viewModel.navigationBarColor,
        setDialogStatus = { newState ->
            viewModel.navigationBarColorDialogStatus = newState
        },
        onOkButtonClicked = { color ->
            try {
                Color.parseColor(color)
            } catch (e: Exception) {
                viewModel.openColorInputInvalidAlertStatus = true
                return@WebViewSettingDialog
            }
            viewModel.navigationBarColor = color
        }
    )

    WebViewSettingDialog(
        isDialogOpened = viewModel.cutoutAreaColorDialogStatus,
        title = stringResource(id = R.string.developer_web_view_configuration_cutout_color),
        labelName = stringResource(id = R.string.developer_web_view_configuration_cutout_color_label_name),
        message = viewModel.cutoutAreaColor,
        setDialogStatus = { newState ->
            viewModel.cutoutAreaColorDialogStatus = newState
        },
        onOkButtonClicked = { color ->
            try {
                Color.parseColor(color)
            } catch (e: Exception) {
                viewModel.openColorInputInvalidAlertStatus = true
                return@WebViewSettingDialog
            }
            viewModel.cutoutAreaColor = color
        }
    )

    ConfirmAlertDialog(
        isDialogOpened = viewModel.openColorInputInvalidAlertStatus,
        title = "Failed",
        description = "Color is invalid",
        onOkButtonClicked = {},
        setDialogStatus = { newState ->
            viewModel.openColorInputInvalidAlertStatus = newState
        }
    )

    WebViewSettingDialog(
        isDialogOpened = viewModel.navigationBarHeightDialogStatus,
        title = stringResource(id = R.string.developer_web_view_configuration_navigation_bar_height),
        labelName = stringResource(id = R.string.developer_web_view_configuration_navigation_bar_height_label_name),
        message = viewModel.navigationBarHeight.toString(),
        setDialogStatus = { newState ->
            viewModel.navigationBarHeightDialogStatus = newState
        },
        onOkButtonClicked = { height ->
            viewModel.navigationBarHeight = height.toInt()
        }
    )

    OpenCustomWebViewDialog(
        isDialogOpened = viewModel.openWebViewDialogStatus,
        title = stringResource(R.string.developer_web_view_show),
        fieldMessage = WEBVIEW_MENU_DEFAULT_URL,
        setDialogStatus = { newState ->
            viewModel.openWebViewDialogStatus = newState
        },
        onOkButtonClicked = { url ->
            viewModel.openWebView(activity = activity, urlString = url)
        }
    )
}