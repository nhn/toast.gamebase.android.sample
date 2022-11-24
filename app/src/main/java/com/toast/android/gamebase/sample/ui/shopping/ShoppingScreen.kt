/*
 * © NHN Corp. All rights reserved.
 * NHN Corp. PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.toast.android.gamebase.sample.ui.shopping

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.toast.android.gamebase.sample.R
import com.toast.android.gamebase.base.purchase.PurchasableItem
import com.toast.android.gamebase.sample.ui.components.screen.EmptyListScreen
import com.toast.android.gamebase.sample.ui.components.screen.ErrorScreen
import com.toast.android.gamebase.sample.ui.components.screen.LoadingDialog
import com.toast.android.gamebase.sample.ui.theme.*

@Composable
fun ShoppingScreen(
    activity: Activity,
    shoppingViewModel: ShoppingViewModel = viewModel(
        factory = ShoppingViewModelFactory(ShoppingRepository())
    )
) {
    val lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(Unit) {
        shoppingViewModel.requestItemList(activity)
    }
    DisposableEffect(lifecycleOwner) {
        onDispose {
            shoppingViewModel.cancelRequestItemList()
        }
    }
    when (shoppingViewModel.uiState.value) {
        ShoppingUIState.REQUEST_SUCCESS -> ShoppingRequestSuccessScreen(
            activity = activity,
            shoppingViewModel = shoppingViewModel
        )
        ShoppingUIState.REQUEST_ERROR -> {
            ErrorScreen(errorString = stringResource(id = R.string.request_shopping_list_error))
        }
        ShoppingUIState.REQUEST_LOADING -> LoadingDialog {}
        ShoppingUIState.EMPTY_ITEM -> EmptyListScreen()
    }
}

@Composable
fun ShoppingRequestSuccessScreen(activity: Activity, shoppingViewModel: ShoppingViewModel) {
    if (shoppingViewModel.needLoadingDialog) {
        LoadingDialog {
            shoppingViewModel.needLoadingDialog = false
        }
    }
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(White)
    ) {
        LazyColumn {
            items(items = shoppingViewModel.itemList) { item ->
                ListItems(activity = activity, item = item, shoppingViewModel = shoppingViewModel)
            }
        }
    }
}

@Composable
fun ListItems(
    activity: Activity,
    item: PurchasableItem,
    shoppingViewModel: ShoppingViewModel
) {
    Row(
        modifier = Modifier
            .clickable(onClick = {
                if (!item.gamebaseProductId.isNullOrEmpty()) {
                    shoppingViewModel.requestItemNotConsumed(activity)
                    shoppingViewModel.requestPurchaseItem(activity, item.gamebaseProductId!!)
                    shoppingViewModel.requestItemNotConsumed(activity)
                }
                shoppingViewModel.needLoadingDialog = true
            })
            .fillMaxWidth()
            .padding(
                horizontal = dimensionResource(id = R.dimen.shopping_screen_lazy_column_horizontal_padding),
                vertical = dimensionResource(id = R.dimen.shopping_screen_lazy_column_vertical_padding)
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(
                text = item.itemName ?: "",
                style = ShoppingTypography.body1,
            )
            Text(
                text = item.productType,
                style = ShoppingTypography.subtitle1
            )
        }
        Box(
            modifier = Modifier.height(dimensionResource(id = R.dimen.shopping_screen_lazy_column_box_height)),
            Alignment.Center
        ) {
            Text(
                text = item.localizedPrice ?: "",
                style = ShoppingTypography.body2
            )
        }
    }
}