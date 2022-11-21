package com.toast.android.gamebase.sample.ui

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.toast.android.gamebase.sample.R
import com.toast.android.gamebase.base.purchase.PurchasableItem
import com.toast.android.gamebase.sample.ui.components.LoadingDialog
import com.toast.android.gamebase.sample.ui.components.EmptyListScreen
import com.toast.android.gamebase.sample.ui.components.ErrorScreen
import com.toast.android.gamebase.sample.ui.shopping.ShoppingUIState
import com.toast.android.gamebase.sample.ui.shopping.ShoppingViewModel
import com.toast.android.gamebase.sample.ui.shopping.observeShoppingLifecycle
import com.toast.android.gamebase.sample.ui.shopping.*
import com.toast.android.gamebase.sample.ui.theme.Black
import com.toast.android.gamebase.sample.ui.theme.Grey500
import com.toast.android.gamebase.sample.ui.theme.White

@Composable
fun ShoppingScreen(
    activity: Activity,
    shoppingViewModel: ShoppingViewModel = viewModel(
        factory = ShoppingViewModelFactory(ShoppingRepository())
    )
) {
    shoppingViewModel.setGamebaseActivity(activity)
    shoppingViewModel.observeShoppingLifecycle(LocalLifecycleOwner.current.lifecycle)

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
        LazyColumn() {
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
            .fillMaxWidth()
            .padding(
                horizontal = dimensionResource(id = R.dimen.shopping_screen_lazy_column_horizontal_padding),
                vertical = dimensionResource(id = R.dimen.shopping_screen_lazy_column_vertical_padding)
            )
            .clickable(onClick = {
                if (!item.gamebaseProductId.isNullOrEmpty()) {
                    shoppingViewModel.requestItemNotConsumed(activity)
                    shoppingViewModel.requestPurchaseItem(activity, item.gamebaseProductId!!)
                    shoppingViewModel.requestItemNotConsumed(activity)
                }
                shoppingViewModel.needLoadingDialog = true
            }),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column() {
            Text(
                text = item.itemName ?: "",
                color = Black,
                fontSize = 10.sp,
                fontWeight = FontWeight(400),
                textAlign = TextAlign.Center
            )
            Text(
                text = item.productType,
                color = Grey500,
                fontSize = 8.sp,
                fontWeight = FontWeight(400),
                textAlign = TextAlign.Center
            )
        }

        Box(modifier = Modifier.height(dimensionResource(id = R.dimen.shoppign_screen_lazy_column_box_height)), Alignment.Center) {
            Text(
                text = item.localizedPrice ?: "",
                color = Black,
                fontSize = 15.sp,
                fontWeight = FontWeight(400),
                textAlign = TextAlign.Center
            )
        }
    }
}