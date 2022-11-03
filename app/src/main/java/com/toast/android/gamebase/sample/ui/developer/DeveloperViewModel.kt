package com.toast.android.gamebase.sample.ui.developer

import android.app.Activity
import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.toast.android.gamebase.base.purchase.PurchasableReceipt
import com.toast.android.gamebase.sample.GamebaseApplication
import com.toast.android.gamebase.sample.R
import com.toast.android.gamebase.sample.gamebasemanager.cancelWithdrawal
import com.toast.android.gamebase.sample.gamebasemanager.isSuccess
import com.toast.android.gamebase.sample.gamebasemanager.queryTokenInfo
import com.toast.android.gamebase.sample.gamebasemanager.requestActivatedPurchases
import com.toast.android.gamebase.sample.gamebasemanager.requestItemListOfNotConsumed
import com.toast.android.gamebase.sample.gamebasemanager.requestWithdrawal
import com.toast.android.gamebase.sample.gamebasemanager.showAlert
import com.toast.android.gamebase.sample.ui.navigation.SampleAppScreens

class DeveloperViewModel: ViewModel() {
    val showPurchaseDialog = mutableStateOf(false)
    var purchaseItemList = mutableListOf<PurchasableReceipt>()
        private set
    val menuMap: MutableMap<String, List<DeveloperMenu>> = createMenuMap()

    private val failedTitle: String = GamebaseApplication.instance.applicationContext.getString(R.string.failed)
    private val successTitle: String = GamebaseApplication.instance.applicationContext.getString(R.string.success)

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


    fun onMenuClick(activity: Activity, developerMenuItem: DeveloperMenu, navController: NavController) {
        when (developerMenuItem.id) {
            DeveloperMenu.AUTH_SUSPEND_WITHDRAWAL -> requestWithdrawal(activity)
            DeveloperMenu.AUTH_SUSPEND_WITHDRAWAL_CANCEL -> cancelWithdrawal(activity)
            DeveloperMenu.PURCHASE_ACTIVATED_SUBSCRIPTION -> fetchActivatedPurchaseList(activity)
            DeveloperMenu.PURCHASE_NOT_CONSUMED_LIST -> fetchItemNotConsumedList(activity)
            DeveloperMenu.PUSH_CURRENT_SETTING -> fetchPushCurrentSetting(activity)
            DeveloperMenu.PUSH_DETAIL_SETTING -> {
                navController.navigate(SampleAppScreens.DeveloperPushSetting.route)
            }
        }
    }

    private fun requestWithdrawal(activity: Activity) {
        val context = GamebaseApplication.instance.applicationContext
        requestWithdrawal(activity) { data, exception ->
            if (isSuccess(exception)) {
                showAlert(
                    activity,
                    successTitle,
                    context.resources.getString(R.string.request_withdrawal_success) + "${data.gracePeriodDate}"
                )
            } else {
                showAlert(
                    activity,
                    failedTitle,
                    exception.toJsonString()
                )
            }
        }
    }

    private fun cancelWithdrawal(activity: Activity) {
        val context = GamebaseApplication.instance.applicationContext
        cancelWithdrawal(activity) { exception ->
            if (isSuccess(exception)) {
                showAlert(
                    activity,
                    successTitle,
                    context.resources.getString(R.string.cancel_withdrawal_success)
                )
            } else {
                showAlert(
                    activity,
                    failedTitle,
                    exception.toJsonString()
                )
            }
        }
    }

    private fun fetchActivatedPurchaseList(activity: Activity) {
        val context = activity as Context
        requestActivatedPurchases(activity) { list, exception ->
            if (isSuccess(exception)) {
                purchaseItemList = list as MutableList<PurchasableReceipt>
                showPurchaseDialog.value = true
            } else {
                showAlert(activity, context.resources.getString(R.string.failed), exception.toJsonString())
            }
        }
    }

    private fun fetchItemNotConsumedList(activity: Activity) {
        val context = activity as Context
        requestItemListOfNotConsumed(activity) { list, exception ->
            if (isSuccess(exception)) {
                purchaseItemList = list as MutableList<PurchasableReceipt>
                showPurchaseDialog.value = true
            } else {
                showAlert(activity, context.resources.getString(R.string.failed), exception.toJsonString())
            }
        }
    }

    private fun fetchPushCurrentSetting(activity: Activity) {
        val context = activity as Context
        queryTokenInfo(activity) { pushTokenInfo, exception ->
            if (isSuccess(exception)) {
                showAlert(activity, successTitle, pushTokenInfo.toJsonString())
            } else {
                showAlert(activity, failedTitle, exception.toJsonString())
            }
        }
    }
}