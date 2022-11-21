package com.toast.android.gamebase.sample.ui.splash

import android.content.ActivityNotFoundException
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.toast.android.gamebase.Gamebase
import com.toast.android.gamebase.sample.gamebasemanager.initializeGamebase
import com.toast.android.gamebase.sample.gamebasemanager.isSuccess
import com.toast.android.gamebase.sample.gamebasemanager.logout
import com.toast.android.gamebase.sample.gamebasemanager.showAlert
import com.toast.android.gamebase.sample.gamebasemanager.showTermsView
import com.toast.android.gamebase.sample.GamebaseActivity
import com.toast.android.gamebase.sample.util.savePushConfiguration
import com.toast.android.gamebase.terms.data.GamebaseShowTermsViewResult

private const val TAG = "SplashScreen"
private const val INITIALIZE_RETRY_MAX_COUNT = 2
private const val MARKET_INTENT_REQUEST_CODE = 123

class SplashViewModel : ViewModel() {
    val isInitialized = mutableStateOf(false)
    private var reInitializeCount = 0

    fun initialize(activity: GamebaseActivity) {
        if (Gamebase.isInitialized()) {
            isInitialized.value = true
            return
        }

        initializeGamebase(
            activity = activity,
            onLaunchingSuccess = {
                showTermsViewPopup(activity) {
                    isInitialized.value = true
                }
            },
            showErrorAndRetryInitialize = { title, message ->
                showErrorAndRetryInitialize(activity, title, message)
            },
            showUnregisteredVersionAndMoveToStore = { updateUrl, message ->
                showUnregisteredVersionAndMoveToStore(activity, updateUrl, message)
            }
        )
    }

    private fun showErrorAndRetryInitialize(
        activity: GamebaseActivity,
        title: String?,
        message: String?
    ) {
        if (title.isNullOrEmpty() || message.isNullOrEmpty()) {
            logoutOrRetryInitialize(activity)
        } else {
            val returnToTitle =
                DialogInterface.OnClickListener { dialog: DialogInterface?, which: Int ->
                    logoutOrRetryInitialize(activity)
                }
            showAlert(activity, title, message, returnToTitle)
        }
    }


    private fun logoutOrRetryInitialize(activity: GamebaseActivity) {
        val userId = Gamebase.getUserID()
        if (!userId.isNullOrEmpty()) {
            logout(activity) { isSuccess, errorMessage ->
                if (!isSuccess) {
                    val msg = errorMessage ?: ""
                    showAlert(activity, "Logout Failed", msg)
                }
            }
        } else {
            retryInitialize(activity)
        }
    }

    private fun showTermsViewPopup(activity: GamebaseActivity, onPopupClosed: (() -> Unit)?) {
        showTermsView(
            activity
        ) { container, exception ->
            if (isSuccess(exception)) {
                val termsViewResult: GamebaseShowTermsViewResult? =
                    GamebaseShowTermsViewResult.from(container)
                if (termsViewResult != null) {
                    Log.d(TAG, "GamebaseShowTermsViewResult : $termsViewResult")
                    if (termsViewResult.isTermsUIOpened) {
                        savePushConfiguration(termsViewResult.pushConfiguration)
                    }
                }
                onPopupClosed?.invoke()
            } else {
                Log.w(TAG, "showTermsView() failed : $exception")
                // 시간 텀을 두고 약관팝업을 다시 보여주도록 구현할 수 있습니다.
                Thread {
                    try {
                        Thread.sleep(500)
                    } catch (ignored: Exception) {
                    }
                    showTermsViewPopup(activity, onPopupClosed)
                }.start()
            }
        }
    }

    private fun showUnregisteredVersionAndMoveToStore(
        activity: GamebaseActivity,
        updateUrl: String,
        message: String
    ) {
        val moveToStore =
            DialogInterface.OnClickListener { _: DialogInterface?, _: Int ->
                try {
                    val marketIntent = Intent(Intent.ACTION_VIEW)
                    marketIntent.data = Uri.parse(updateUrl)
                    activity.startActivityForResult(marketIntent, MARKET_INTENT_REQUEST_CODE)
                } catch (ignored: ActivityNotFoundException) {
                }
            }
        showAlert(
            activity,
            "Unregistered Game Version",
            message,
            moveToStore
        )
    }

    private fun retryInitialize(activity: GamebaseActivity) {
        if (reInitializeCount < INITIALIZE_RETRY_MAX_COUNT) {
            try {
                Thread.sleep(1000)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            ++reInitializeCount
            initialize(activity)
        }
    }
}