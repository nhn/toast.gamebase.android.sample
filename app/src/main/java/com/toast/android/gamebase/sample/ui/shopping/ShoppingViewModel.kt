package com.toast.android.gamebase.sample.ui.shopping

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.*
import androidx.lifecycle.*
import com.toast.android.gamebase.base.purchase.PurchasableItem
import com.toast.android.gamebase.sample.GamebaseActivity
import com.toast.android.gamebase.sample.GamebaseManager
import kotlinx.coroutines.launch

private const val TAG: String = "ShoppingScreen"

@SuppressLint("StaticFieldLeak")
class ShoppingViewModel : ViewModel(), DefaultLifecycleObserver {
    var itemList: List<PurchasableItem> by mutableStateOf(mutableListOf())
        private set
    var needLoadingDialog: Boolean by mutableStateOf(false)
    // TODO : viewModel에서 activity를 멤버 변수로 가지고 있는 사항 수정
    lateinit var mActivity: GamebaseActivity

    override fun onStart(owner: LifecycleOwner) {
        viewModelScope.launch {
            requestItemList(mActivity)
        }
    }

    fun requestItemNotConsumed(activity: GamebaseActivity) {
        GamebaseManager.requestNotConsumedItems(activity) { data, exception ->
            if (!GamebaseManager.isRequestSuccess(exception)) {
                GamebaseManager.showAlert(
                    activity,
                    "requestItemNotConsumed error",
                    exception.toJsonString()
                )
                Log.d(
                    TAG,
                    exception.toJsonString()
                )
            }
        }
    }

    private fun requestItemList(activity: GamebaseActivity) {
        GamebaseManager.requestItemList(activity = activity) { data, exception ->
            if (GamebaseManager.isRequestSuccess(exception)) {
                itemList = data
            } else {
                GamebaseManager.showAlert(
                    activity,
                    "requestItemListPurchasable error",
                    exception.toJsonString()
                )
                Log.d(
                    TAG,
                    exception.toJsonString()
                )
            }
        }
    }

    fun requestPurchaseItem(activity: GamebaseActivity, gamebaseProductId: String) {
        GamebaseManager.requestPurchase(activity, gamebaseProductId) { data, exception ->
            if (GamebaseManager.isRequestSuccess(exception)) {
                GamebaseManager.showToast(activity, "Success Purchase : $data", Toast.LENGTH_SHORT)
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
                GamebaseManager.showAlert(activity, "Error", exception.toJsonString())
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

    fun setGamebaseActivity(activity: GamebaseActivity) {
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