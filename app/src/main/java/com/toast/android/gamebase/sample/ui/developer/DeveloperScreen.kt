package com.toast.android.gamebase.sample.ui.developer

import android.app.Activity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.toast.android.gamebase.sample.R
import com.toast.android.gamebase.sample.gamebasemanager.getAppKey
import com.toast.android.gamebase.sample.gamebasemanager.showWebView
import com.toast.android.gamebase.sample.ui.analytics.SetGameUserDataDialog
import com.toast.android.gamebase.sample.ui.components.ClickableText
import com.toast.android.gamebase.sample.ui.components.InputDialog
import com.toast.android.gamebase.sample.ui.components.ListDialog
import com.toast.android.gamebase.sample.ui.components.SubMenuDivider
import com.toast.android.gamebase.sample.ui.logger.LoggerInitializeDialog
import com.toast.android.gamebase.sample.ui.logger.SendLogDialog
import com.toast.android.gamebase.sample.ui.webview.OpenBrowserDialog
import com.toast.android.gamebase.sample.ui.webview.OpenCustomWebViewDialog

const val WEBVIEW_MENU_DEFAULT_URL = "https://gameplatform.nhncloud.com/ko_KR/service/gamebase"

@Composable
fun DeveloperScreen(
    activity: Activity,
    viewModel: DeveloperViewModel = viewModel(),
    menuNavigator: DeveloperMenuNavigator
) {
    Box {
        Surface(color = MaterialTheme.colors.surface) {
            ListDialog(
                viewModel.showPurchaseDialog.value,
                {viewModel.showPurchaseDialog.value = false},
                viewModel.purchaseItemList,
                {}
            )
            DeveloperMenuList(
                groupedListMap = viewModel.menuMap,
                activity,
                viewModel,
                menuNavigator
            )
        }
    }
    LoggerInitializeDialog(
        activity = activity,
        isDialogOpened = viewModel.isLoggerInitializeOpened.value,
        title = stringResource(id = R.string.logger_initialize),
        message = getAppKey(),
        setDialogStatus = { newState ->
            viewModel.isLoggerInitializeOpened.value = newState
        },
        isLoggerAppKeyValid = getAppKey().isNotEmpty()
    )
    SendLogDialog(
        isDialogOpened = viewModel.isSendLogOpened.value,
        title = stringResource(id = R.string.send_logger),
        setDialogStatus = { newState ->
            viewModel.isSendLogOpened.value = newState
        },
        stringArrayResources = R.array.logger_level
    )
    OpenCustomWebViewDialog(
        isDialogOpened = viewModel.isOpenWebViewOpened.value,
        title = stringResource(id = R.string.developer_menu_open_webview),
        fieldMessage = WEBVIEW_MENU_DEFAULT_URL,
        setDialogStatus = { newState ->
            viewModel.isOpenWebViewOpened.value = newState
        },
        onOkButtonClicked = { value ->
            showWebView(activity, value)
        }
    )
    OpenBrowserDialog(
        activity = activity,
        isDialogOpened = viewModel.isOpenWebBrowserOpened.value,
        title = stringResource(id = R.string.developer_menu_open_outside_browser),
        fieldMessage = WEBVIEW_MENU_DEFAULT_URL,
        setDialogStatus = { newState ->
            viewModel.isOpenWebBrowserOpened.value = newState
        }
    )
    SetGameUserDataDialog(activity = activity,
        isDialogOpened = viewModel.isUserLevelInfoSettingOpened.value,
        title = stringResource(
            id = R.string.developer_analytics_level_setting_title
        ),
        setDialogStatus = { newState ->
            viewModel.isUserLevelInfoSettingOpened.value = newState
        })
    if (viewModel.isUserLevelUpInfoSettingOpened.value) {
        InputDialog(
            title = stringResource(id = R.string.developer_analytics_level_up_setting_title),
            labelName = stringResource(id = R.string.developer_analytics_level_label_name),
            fieldMessage = "",
            setDialogStatus = { newState ->
                viewModel.isUserLevelUpInfoSettingOpened.value = newState
            },
            onOkButtonClicked = { value ->
                viewModel.updateOnLevelUp(activity, value)
            }
        )
    }
}

@Composable
fun DeveloperMenuList(
    groupedListMap: Map<String, List<DeveloperMenu>>,
    activity: Activity,
    viewModel: DeveloperViewModel,
    menuNavigator: DeveloperMenuNavigator
) {
    LazyColumn(modifier = Modifier.padding(horizontal = 20.dp)) {
        groupedListMap.forEach { (category, subMenuList) ->
            item {
                SubMenuDivider(category)
            }
            items(items = subMenuList) { subMenu ->
                MenuItem(subMenu, activity, viewModel, menuNavigator)
            }
        }
    }
}

@Composable
fun MenuItem(developerMenuItem: DeveloperMenu,
             activity: Activity,
             viewModel: DeveloperViewModel,
             menuNavigator: DeveloperMenuNavigator
) {

    Surface (
        color = MaterialTheme.colors.surface,
    ) {
        ClickableText(
            text = developerMenuItem.name,
            style = MaterialTheme.typography.body1
        ) {
            viewModel.onMenuClick(activity, developerMenuItem, menuNavigator)
        }
    }
}
