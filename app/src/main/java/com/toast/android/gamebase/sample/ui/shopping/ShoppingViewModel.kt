/*
 * © NHN Corp. All rights reserved.
 * NHN Corp. PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.toast.android.gamebase.sample.ui.shopping

import android.app.Activity
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.*
import androidx.lifecycle.*
import com.toast.android.gamebase.base.GamebaseException
import com.toast.android.gamebase.base.purchase.PurchasableItem
import com.toast.android.gamebase.sample.gamebase_manager.isSuccess
import com.toast.android.gamebase.sample.gamebase_manager.requestNotConsumedItems
import com.toast.android.gamebase.sample.gamebase_manager.requestPurchase
import com.toast.android.gamebase.sample.gamebase_manager.showAlert
import com.toast.android.gamebase.sample.gamebase_manager.showToast
import com.toast.android.gamebase.sample.util.printWithIndent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

private const val TAG: String = "ShoppingScreen"

enum class ShoppingUIState {
    REQUEST_SUCCESS,
    REQUEST_ERROR,
    REQUEST_LOADING,
    EMPTY_ITEM
}

class ShoppingViewModel(private val shoppingRepository: ShoppingRepository) : ViewModel() {
    private val supervisorJob = SupervisorJob()
    var itemList: List<PurchasableItem> by mutableStateOf(mutableListOf())
        private set
    var needLoadingDialog: Boolean by mutableStateOf(false)
    val uiState = mutableStateOf(ShoppingUIState.REQUEST_LOADING)

    suspend fun requestItemList(activity: Activity) {
        viewModelScope.launch(Dispatchers.IO + supervisorJob) {
            uiState.value = ShoppingUIState.REQUEST_LOADING
            try {
                itemList = shoppingRepository.getItemsList(activity = activity)
                if (itemList.isEmpty()) {
                    uiState.value = ShoppingUIState.EMPTY_ITEM
                } else {
                    uiState.value = ShoppingUIState.REQUEST_SUCCESS
                }
            } catch (exception: GamebaseException) {
                showAlert(
                    activity,
                    "requestItemListPurchasable error",
                    exception.printWithIndent()
                )
                Log.d(
                    TAG,
                    exception.toJsonString()
                )
                uiState.value = ShoppingUIState.REQUEST_ERROR
            }
        }
    }

    fun requestItemNotConsumed(activity: Activity) {
        requestNotConsumedItems(activity) { data, exception ->
            if (!isSuccess(exception)) {
                showAlert(
                    activity,
                    "requestItemNotConsumed error",
                    exception.printWithIndent()
                )
                Log.d(
                    TAG,
                    exception.toJsonString()
                )
            }
        }
    }

    fun requestPurchaseItem(activity: Activity, gamebaseProductId: String) {
        requestPurchase(activity, gamebaseProductId) { data, exception ->
            if (isSuccess(exception)) {
                showToast(activity, "Success Purchase : $data", Toast.LENGTH_SHORT)
                needLoadingDialog = false
                if (data != null) {
                    Log.d(TAG, data.toJsonString())
                }
            } else {
                showAlert(activity, "Error", exception.printWithIndent())
                needLoadingDialog = false
                Log.d(
                    TAG,
                    exception.toJsonString()
                )
            }
        }
    }

    fun cancelRequestItemList() {
        supervisorJob.cancel()
    }
}
