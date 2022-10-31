package com.toast.android.gamebase.sample.ui.developer

import android.app.Activity
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.toast.android.gamebase.base.purchase.PurchasableReceipt
import com.toast.android.gamebase.sample.GamebaseApplication
import com.toast.android.gamebase.sample.R
import com.toast.android.gamebase.sample.gamebasemanager.isSuccess
import com.toast.android.gamebase.sample.gamebasemanager.requestActivatedPurchases
import com.toast.android.gamebase.sample.gamebasemanager.requestItemListOfNotConsumed
import com.toast.android.gamebase.sample.gamebasemanager.showAlert

class DeveloperViewModel: ViewModel() {
    val menuMap = createMenuMap()

    val showPurchaseDialog = mutableStateOf(false)
    var purchaseItemList = mutableListOf<PurchasableReceipt>()
        private set

    private fun createMenuMap(): MutableMap<String, List<Menu>> {
        val menuMap: MutableMap<String, List<Menu>> = mutableMapOf()
        val context = GamebaseApplication.instance.applicationContext

        menuMap[context.resources.getString(R.string.developer_menu_category_auth)] =
            context.resources.getStringArray(R.array.Auth).mapIndexed() { index, item ->
                Menu(R.array.Auth, index, item)
            }
        menuMap[context.resources.getString(R.string.developer_menu_category_purchase)] =
            context.resources.getStringArray(R.array.Purchase).mapIndexed() { index, item ->
                Menu(R.array.Purchase, index, item)
            }
        menuMap[context.resources.getString(R.string.developer_menu_category_push)] =
            context.resources.getStringArray(R.array.Push).mapIndexed() { index, item ->
                Menu(R.array.Push, index, item)
            }
        menuMap[context.resources.getString(R.string.developer_menu_category_logger)] =
            context.resources.getStringArray(R.array.Logger).mapIndexed() { index, item ->
                Menu(R.array.Logger, index, item)
            }
        menuMap[context.resources.getString(R.string.developer_menu_category_terms)] =
            context.resources.getStringArray(R.array.Terms).mapIndexed() { index, item ->
                Menu(R.array.Terms, index, item)
            }
        menuMap[context.resources.getString(R.string.developer_menu_category_image_notice)] =
            context.resources.getStringArray(R.array.ImageNotice).mapIndexed() { index, item ->
                Menu(R.array.ImageNotice, index, item)
            }
        menuMap[context.resources.getString(R.string.developer_menu_category_webview)] =
            context.resources.getStringArray(R.array.WebView).mapIndexed() { index, item ->
                Menu(R.array.WebView, index, item)
            }
        menuMap[context.resources.getString(R.string.developer_menu_category_alert)] =
            context.resources.getStringArray(R.array.Alert).mapIndexed() { index, item ->
                Menu(R.array.Alert, index, item)
            }
        menuMap[context.resources.getString(R.string.developer_menu_category_analytics)] =
            context.resources.getStringArray(R.array.Analytics).mapIndexed() { index, item ->
                Menu(R.array.Analytics, index, item)
            }
        menuMap[context.resources.getString(R.string.developer_menu_category_contact)] =
            context.resources.getStringArray(R.array.Contact).mapIndexed() { index, item ->
                Menu(R.array.Contact, index, item)
            }
        menuMap[context.resources.getString(R.string.developer_menu_category_etc)] =
            context.resources.getStringArray(R.array.Etc).mapIndexed() { index, item ->
                Menu(R.array.Etc, index, item)
            }
        return menuMap
    }

    fun onMenuClick(activity: Activity, menuItem: Menu) {
        if (menuItem.category == R.array.Purchase) {
            if (menuItem.id == 0) {
                fetchActivatedPurchaseList(activity)
            } else if (menuItem.id == 1) {
                fetchItemNotConsumedList(activity)
            }
        }
        // TODO: More Menus
    }

    private fun fetchActivatedPurchaseList(activity: Activity) {
        requestActivatedPurchases(activity) { list, exception ->
            if (isSuccess(exception)) {
                purchaseItemList = list as MutableList<PurchasableReceipt>
                showPurchaseDialog.value = true
            } else {
                showAlert(activity, "Fetch activated purchase item failed", exception.toJsonString())
            }
        }
    }

    private fun fetchItemNotConsumedList(activity: Activity) {
        requestItemListOfNotConsumed(activity) { list, exception ->
            if (isSuccess(exception)) {
                purchaseItemList = list as MutableList<PurchasableReceipt>
                showPurchaseDialog.value = true
            } else {
                showAlert(activity, "Fetch not consumed item failed", exception.toJsonString())
            }
        }
    }
}
