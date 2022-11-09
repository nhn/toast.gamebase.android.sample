package com.toast.android.gamebase.sample.ui.developer

import android.app.Activity
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.toast.android.gamebase.sample.ui.common.ListDialog
import com.toast.android.gamebase.sample.ui.common.SubMenuDivider
import com.toast.android.gamebase.sample.R
import com.toast.android.gamebase.sample.ui.common.LoggerInitializeDialog
import com.toast.android.gamebase.sample.ui.common.SendLogDialog

@Composable
fun DeveloperScreen(
    activity: Activity,
    viewModel: DeveloperViewModel = viewModel(),
    navController: NavController
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
                navController
            )
        }
    }
    LoggerInitializeDialog(
        isDialogOpened = viewModel.isLoggerInitializeOpened.value,
        title = stringResource(id = R.string.logger_initialize),
        setDialogStatus = { newState ->
            viewModel.isLoggerInitializeOpened.value = newState
        },
        viewModel.loggerAppKey,
        onOkButtonClicked = { text ->
            viewModel.initializeLogger(activity, text)
            viewModel.refreshAppKey()
        },
        onCancelButtonClicked = {
            viewModel.refreshAppKey()
        },
        isLoggerAppKeyValid = viewModel.isLoggerAppKeyValid
    )
    SendLogDialog(
        isDialogOpened = viewModel.isSendLogOpened.value,
        title = stringResource(id = R.string.send_logger),
        setDialogStatus = { newState ->
            viewModel.isSendLogOpened.value = newState
        },
        loggerInformation = viewModel.loggerInformation,
        loggerLevelExpanded = viewModel.loggerLevelExpanded,
        onOkButtonClicked = { loggerInformation ->
            viewModel.sendLogger(loggerInformation)
            viewModel.refreshLoggerInformation()
        },
        onCancelButtonClicked = {
            viewModel.refreshLoggerInformation()
        },
        stringArrayResources = R.array.logger_level
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DeveloperMenuList(
    groupedListMap: Map<String, List<DeveloperMenu>>,
    activity: Activity,
    viewModel: DeveloperViewModel,
    navController: NavController
) {
    LazyColumn(modifier = Modifier.padding(horizontal = 20.dp)) {
        groupedListMap.forEach { (category, subMenuList) ->
            item {
                SubMenuDivider(category)
            }
            items(items = subMenuList) { subMenu ->
                MenuItem(subMenu, activity, viewModel, navController)
            }
        }
    }
}

@Composable
fun MenuItem(developerMenuItem: DeveloperMenu,
             activity: Activity,
             viewModel: DeveloperViewModel,
             navController: NavController
) {
    Surface (
        color = MaterialTheme.colors.surface,
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                viewModel.onMenuClick(activity, developerMenuItem, navController)
            }
            .padding(vertical = 12.dp, horizontal = 10.dp)
    ) {
        Text(
            text = developerMenuItem.name,
            style = MaterialTheme.typography.body1)
    }
}
