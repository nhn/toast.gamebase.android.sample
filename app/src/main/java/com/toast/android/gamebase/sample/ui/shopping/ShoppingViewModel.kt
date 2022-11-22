package com.toast.android.gamebase.sample.ui.shopping

import android.annotation.SuppressLint
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

enum class ShoppingUIState() {
    REQUEST_SUCCESS,
    REQUEST_ERROR,
    REQUEST_LOADING,
    EMPTY_ITEM
}

@SuppressLint("StaticFieldLeak")
class ShoppingViewModel(private val shoppingRepository: ShoppingRepository) : ViewModel(),
    DefaultLifecycleObserver {
    private val supervisorJob = SupervisorJob()
    var itemList: List<PurchasableItem> by mutableStateOf(mutableListOf())
        private set
    var needLoadingDialog: Boolean by mutableStateOf(false)
    val uiState = mutableStateOf(ShoppingUIState.REQUEST_LOADING)

    // TODO : viewModel에서 activity를 멤버 변수로 가지고 있는 사항 수정
    lateinit var mActivity: Activity

    override fun onStart(owner: LifecycleOwner) {
        viewModelScope.launch(Dispatchers.IO + supervisorJob) {
            uiState.value = ShoppingUIState.REQUEST_LOADING
            try {
                itemList = shoppingRepository.getItemsList(activity = mActivity)
                if (itemList.isEmpty()) {
                    uiState.value = ShoppingUIState.EMPTY_ITEM
                } else {
                    uiState.value = ShoppingUIState.REQUEST_SUCCESS
                }
            } catch (exception: GamebaseException) {
                showAlert(
                    mActivity,
                    "requestItemListPurchasable error",
                    exception.printWithIndent()
                )
                Log.d(
                    TAG,
                    exception.toJsonString()
                )
                ShoppingUIState.REQUEST_ERROR
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        supervisorJob.cancel()
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
                    Log.d(TAG, data.paymentSeq)
                    Log.d(TAG, data.currency)
                    Log.d(
                        TAG,
                        data.purchaseToken ?: "purchaseToken : null"
                    )
                    Log.d(
                        TAG,
                        data.gamebaseProductId ?: "gamebaseProductId : null"
                    )
                    Log.d(
                        TAG,
                        data.price.toString()
                    )
                }
            } else {
                showAlert(activity, "Error", exception.printWithIndent())
                needLoadingDialog = false
                Log.d(
                    TAG,
                    exception.toJsonString()
                )
                Log.d(TAG, exception.domain)
                Log.d(
                    TAG,
                    exception.code.toString()
                )
                Log.d(
                    TAG,
                    exception.message ?: "message : null"
                )
                Log.d(
                    TAG,
                    exception.detailDomain ?: "detailDomain : null"
                )
                Log.d(
                    TAG,
                    exception.detailCode.toString()
                )
                Log.d(
                    TAG,
                    exception.detailMessage ?: "detailMessage : null"
                )
            }
        }
    }

    fun setGamebaseActivity(activity: Activity) {
        this.mActivity = activity
    }
}

@Composable
fun <lifecycleObserver : LifecycleObserver> lifecycleObserver.observeShoppingLifecycle(
    lifecycle: Lifecycle
) {
    DisposableEffect(lifecycle) {
        lifecycle.addObserver(this@observeShoppingLifecycle)
        onDispose {
            lifecycle.removeObserver(this@observeShoppingLifecycle)
        }
    }
}