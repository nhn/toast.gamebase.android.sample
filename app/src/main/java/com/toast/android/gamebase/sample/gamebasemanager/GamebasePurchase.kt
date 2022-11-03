package com.toast.android.gamebase.sample.gamebasemanager

import android.app.Activity
import android.util.Log
import com.toast.android.gamebase.Gamebase
import com.toast.android.gamebase.GamebaseDataCallback
import com.toast.android.gamebase.base.GamebaseError
import com.toast.android.gamebase.base.purchase.PurchasableItem
import com.toast.android.gamebase.base.purchase.PurchasableReceipt

fun requestNotConsumedItems(
    activity: Activity,
    callback: GamebaseDataCallback<List<PurchasableReceipt>>?
) {
    Gamebase.Purchase.requestItemListOfNotConsumed(
        activity
    ) { purchasableReceipts, exception ->
        if (Gamebase.isSuccess(exception)) {
            // Succeeded.
        } else {
            // Failed.
            Log.w(TAG, "Request purchases failed : $exception")
        }
        callback?.onCallback(purchasableReceipts, exception)
    }
}

fun requestItemList(
    activity: Activity,
    callback: GamebaseDataCallback<List<PurchasableItem>>?
) {
    Gamebase.Purchase.requestItemListPurchasable(
        activity
    ) { purchasableItems, exception ->
        if (Gamebase.isSuccess(exception)) {
            // Succeeded.
        } else {
            // Failed.
            Log.w(TAG, "Request item list failed : $exception")
        }
        callback?.onCallback(purchasableItems, exception)
    }
}

fun requestPurchase(
    activity: Activity,
    gamebaseProductId: String,
    callback: GamebaseDataCallback<PurchasableReceipt>?
) {
    Gamebase.Purchase.requestPurchase(
        activity, gamebaseProductId
    ) { data, exception ->
        if (Gamebase.isSuccess(exception)) {
            // Succeeded.
        } else if (exception.code == GamebaseError.PURCHASE_USER_CANCELED) {
            // User canceled.
        } else {
            // To Purchase Item Failed cause of the error
        }
        callback?.onCallback(data, exception)
    }
}

fun requestActivatedPurchases(
    activity: Activity,
    callback: GamebaseDataCallback<List<PurchasableReceipt>>?
) {
    Gamebase.Purchase.requestActivatedPurchases(activity) { list, exception ->
        callback?.onCallback(list, exception)
    }
}

fun requestItemListOfNotConsumed(
    activity: Activity,
    callback: GamebaseDataCallback<List<PurchasableReceipt>>?
) {
    Gamebase.Purchase.requestItemListOfNotConsumed(activity) { data, exception ->
        callback?.onCallback(data, exception)
    }
}