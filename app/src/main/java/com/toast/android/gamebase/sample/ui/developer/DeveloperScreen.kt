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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.Navigator
import com.toast.android.gamebase.sample.ui.common.ListDialog

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
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DeveloperMenuList(
    groupedListMap: Map<String, List<DeveloperMenu>>,
    activity: Activity,
    viewModel: DeveloperViewModel,
    navController: NavController
) {
    LazyColumn {
        groupedListMap.forEach { (category, subMenuList) ->
            item {
                CharacterHeader(category)
            }
            items(items = subMenuList) { subMenu ->
                MenuItem(subMenu, activity, viewModel, navController)
            }
        }
    }
}

@Composable
fun CharacterHeader(text: String) {
    Surface(color = MaterialTheme.colors.secondary) {
        Box(modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)) {
            Text(text = text, style = MaterialTheme.typography.body1)
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
            .padding(vertical = 12.dp, horizontal = 8.dp)
    ) {
        Text(
            text = developerMenuItem.name,
            style = MaterialTheme.typography.body1)
    }
}
