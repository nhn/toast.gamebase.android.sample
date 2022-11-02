package com.toast.android.gamebase.sample.ui.developer

import android.app.Activity
import android.util.Log
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
    val showPurchaseDialog = mutableStateOf(false)
    var purchaseItemList = mutableListOf<PurchasableReceipt>()
        private set
    val menuMap: MutableMap<String, List<DeveloperMenu>> = createMenuMap()

    private fun createMenuMap(): MutableMap<String, List<DeveloperMenu>> {
        val developerMenuMap: MutableMap<String, List<DeveloperMenu>> = mutableMapOf()
        val context = GamebaseApplication.instance.applicationContext
        val menuFactory = DeveloperMenuFactory()

        developerMenuMap[context.resources.getString(R.string.developer_menu_category_auth)] =
            menuFactory.createMenu(context, R.array.Auth)
        developerMenuMap[context.resources.getString(R.string.developer_menu_category_purchase)] =
            menuFactory.createMenu(context, R.array.Purchase)
        developerMenuMap[context.resources.getString(R.string.developer_menu_category_push)] =
            menuFactory.createMenu(context, R.array.Push)
        developerMenuMap[context.resources.getString(R.string.developer_menu_category_logger)] =
            menuFactory.createMenu(context, R.array.Logger)
        developerMenuMap[context.resources.getString(R.string.developer_menu_category_terms)] =
            menuFactory.createMenu(context, R.array.Terms)
        developerMenuMap[context.resources.getString(R.string.developer_menu_category_image_notice)] =
            menuFactory.createMenu(context, R.array.ImageNotice)
        developerMenuMap[context.resources.getString(R.string.developer_menu_category_webview)] =
            menuFactory.createMenu(context, R.array.WebView)
        developerMenuMap[context.resources.getString(R.string.developer_menu_category_alert)] =
            menuFactory.createMenu(context, R.array.Alert)
        developerMenuMap[context.resources.getString(R.string.developer_menu_category_analytics)] =
            menuFactory.createMenu(context, R.array.Analytics)
        developerMenuMap[context.resources.getString(R.string.developer_menu_category_contact)] =
            menuFactory.createMenu(context, R.array.Contact)
        developerMenuMap[context.resources.getString(R.string.developer_menu_category_etc)] =
            menuFactory.createMenu(context, R.array.Etc)

        return developerMenuMap
    }

    fun onMenuClick(activity: Activity, developerMenuItem: DeveloperMenu) {
        when(developerMenuItem.id) {
            DeveloperMenu.PURCHASE_ACTIVATED_SUBSCRIPTION -> fetchActivatedPurchaseList(activity)
            DeveloperMenu.PURCHASE_NOT_CONSUMED_LIST -> fetchItemNotConsumedList(activity)
        }
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
