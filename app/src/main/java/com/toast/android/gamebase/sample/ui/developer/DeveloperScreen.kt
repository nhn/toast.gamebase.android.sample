package com.toast.android.gamebase.sample.ui.developer

import android.app.Activity
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.toast.android.gamebase.sample.R
import com.toast.android.gamebase.sample.ui.common.ListDialog
import com.toast.android.gamebase.sample.ui.shopping.ShoppingViewModel

@Composable
fun DeveloperScreen(
    activity: Activity,
    viewModel: DeveloperViewModel = viewModel()
) {
    val menuMap = createMenuMap()
    Box {
        Surface(color = MaterialTheme.colors.surface) {
            // 구독...다얄로그
            ListDialog(
                viewModel.showDialog.value,
                {viewModel.showDialog.value = false},
                viewModel.activatedPurchases,
                {}
            )
            DeveloperMenuList(groupedListMap = menuMap, activity, viewModel)
        }
    }
}

@Composable
private fun createMenuMap(): MutableMap<String, List<String>> {
    val menuMap: MutableMap<String, List<String>> = mutableMapOf()
    menuMap["인증"] = stringArrayResource(id = R.array.인증).toList()
    menuMap["결제"] = stringArrayResource(id = R.array.결제).toList()
    menuMap["푸시"] = stringArrayResource(id = R.array.푸시).toList()
    menuMap["Logger"] = stringArrayResource(id = R.array.Logger).toList()
    menuMap["약관"] = stringArrayResource(id = R.array.약관).toList()
    menuMap["이미지공지"] = stringArrayResource(id = R.array.이미지공지).toList()
    menuMap["웹뷰"] = stringArrayResource(id = R.array.웹뷰).toList()
    menuMap["Alert"] = stringArrayResource(id = R.array.Alert).toList()
    menuMap["Analytics"] = stringArrayResource(id = R.array.Analytics).toList()
    menuMap["고객센터"] = stringArrayResource(id = R.array.고객센터).toList()
    menuMap["기타"] = stringArrayResource(id = R.array.기타).toList()

    return menuMap
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DeveloperMenuList(
    groupedListMap: Map<String, List<String>>,
    activity: Activity,
    viewModel: DeveloperViewModel) {
    LazyColumn {
        groupedListMap.forEach { (category, subMenuList) ->
            item {
                CharacterHeader(category)
            }
            items(items = subMenuList) { subMenu ->
                MenuItem(subMenu, activity, viewModel)
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
fun MenuItem(itemText: String,
             activity: Activity,
             viewModel: DeveloperViewModel) {
    Surface (
        color = MaterialTheme.colors.surface,
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                viewModel.onMenuClick(activity, itemText)
            }
            .padding(vertical = 12.dp, horizontal = 8.dp)
    ) {
        Text(
            text = itemText,
            style = MaterialTheme.typography.body1)
    }
}

val menuCategory = listOf(
    "인증",
    "결제",
    "푸시",
    "Logger",
    "약관",
    "이미지공지",
    "웹뷰",
    "Alert",
    "Analytics",
    "고객센터",
    "기타")