package com.toast.android.gamebase.sample.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import com.toast.android.gamebase.sample.R
import com.toast.android.gamebase.base.purchase.PurchasableItem
import com.toast.android.gamebase.sample.GamebaseActivity
import com.toast.android.gamebase.sample.ui.shopping.ShoppingViewModel
import com.toast.android.gamebase.sample.ui.shopping.observeShoppingLifecycle
import com.toast.android.gamebase.sample.ui.theme.Black
import com.toast.android.gamebase.sample.ui.theme.Grey500
import com.toast.android.gamebase.sample.ui.theme.White

@Composable
fun ShoppingScreen(activity: GamebaseActivity, shoppingViewModel: ShoppingViewModel = viewModel()) {
    if (shoppingViewModel.needLoadingDialog) {
        Dialog(
            onDismissRequest = { shoppingViewModel.needLoadingDialog = false },
            DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(100.dp)
                    .background(White, shape = RoundedCornerShape(12.dp))
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator()
                    Text(
                        text = stringResource(R.string.loading),
                        Modifier.padding(top = 8.dp)
                    )
                }
            }
        }
    }
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(White)
    ) {
        shoppingViewModel.setGamebaseActivity(activity)
        shoppingViewModel.observeShoppingLifecycle(LocalLifecycleOwner.current.lifecycle)
        LazyColumn() {
            items(items = shoppingViewModel.itemList) { item ->
                ListItems(activity = activity, item = item, shoppingViewModel = shoppingViewModel)
            }
        }
    }
}

@Composable
fun ListItems(
    activity: GamebaseActivity,
    item: PurchasableItem,
    shoppingViewModel: ShoppingViewModel
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp, bottom = 10.dp, start = 20.dp, end = 20.dp)
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

        Box(modifier = Modifier.height(30.dp), Alignment.Center) {
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