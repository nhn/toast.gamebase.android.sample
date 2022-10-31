package com.toast.android.gamebase.sample.ui.developer

import android.app.Activity
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.toast.android.gamebase.base.purchase.PurchasableReceipt
import com.toast.android.gamebase.sample.gamebasemanager.isSuccess
import com.toast.android.gamebase.sample.gamebasemanager.requestActivatedPurchases
import com.toast.android.gamebase.sample.gamebasemanager.requestItemListOfNotConsumed
import com.toast.android.gamebase.sample.gamebasemanager.showAlert

class DeveloperViewModel: ViewModel() {
    val showDialog = mutableStateOf(false)
    var activatedPurchases = mutableListOf<PurchasableReceipt>()

    fun onMenuClick(activity: Activity, itemText: String) {
        // TODO: Change to id not string
        if (itemText == "구독 목록 조회") {
            fetchActivatedPurchaseList(activity)
        } else if (itemText == "미소비 목록 조회") {
            fetchActivatedPurchaseList(activity)
        }
    }

    private fun fetchActivatedPurchaseList(activity: Activity) {
        requestActivatedPurchases(activity) { list, exception ->
            if (isSuccess(exception)) {
                activatedPurchases = list as MutableList<PurchasableReceipt>
                showDialog.value = true
            } else {
                showAlert(activity, "Fetch activated purchase item failed", exception.toJsonString())
            }
        }
    }

    private fun fetchItemNotConsumedList(activity: Activity) {
        requestItemListOfNotConsumed(activity) { list, exception ->
            if (isSuccess(exception)) {
                activatedPurchases = list as MutableList<PurchasableReceipt>
                showDialog.value = true
            } else {
                showAlert(activity, "Fetch not consumed item failed", exception.toJsonString())
            }
        }
    }
}
