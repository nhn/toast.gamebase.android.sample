/*
 * © NHN Corp. All rights reserved.
 * NHN Corp. PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.toast.android.gamebase.sample.ui.shopping

import android.app.Activity
import com.toast.android.gamebase.GamebaseDataCallback
import com.toast.android.gamebase.base.purchase.PurchasableItem
import com.toast.android.gamebase.sample.gamebase_manager.isSuccess
import com.toast.android.gamebase.sample.gamebase_manager.requestItemList
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class ShoppingRepository {
    suspend fun getItemsList(activity: Activity): List<PurchasableItem> =
        suspendCancellableCoroutine { continuation ->
            val callback =
                GamebaseDataCallback<List<PurchasableItem>> { data, exception ->
                    if (isSuccess(exception)) {
                        continuation.resume(data)
                    } else {
                        continuation.resumeWithException(exception)
                    }
                }
            requestItemList(activity, callback)
        }
}