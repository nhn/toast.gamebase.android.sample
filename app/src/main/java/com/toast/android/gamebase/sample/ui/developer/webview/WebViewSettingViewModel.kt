package com.toast.android.gamebase.sample.ui.developer.webview

import android.app.Activity
import android.graphics.Color
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.toast.android.gamebase.GamebaseWebViewConfiguration
import com.toast.android.gamebase.base.ScreenOrientation
import com.toast.android.gamebase.sample.GamebaseApplication
import com.toast.android.gamebase.sample.R
import com.toast.android.gamebase.sample.gamebase_manager.isSuccess
import com.toast.android.gamebase.sample.gamebase_manager.showAlert
import com.toast.android.gamebase.sample.gamebase_manager.showWebView

const val TAG = "WebViewSetting"

class WebViewSettingViewModel : ViewModel() {
    var navigationBarVisibleStatus: Boolean by mutableStateOf(false)
    var navigationBarBackButtonStatus: Boolean by mutableStateOf(false)

    var renderOutSafeArea: Boolean by mutableStateOf(false)
    var navigationBarTitleDialogStatus: Boolean by mutableStateOf(false)
    var navigationBarTitle: String by mutableStateOf("")

    var navigationBarColorDialogStatus: Boolean by mutableStateOf(false)
    var navigationBarColor: String by mutableStateOf("#80000000")

    var navigationBarHeightDialogStatus: Boolean by mutableStateOf(false)
    var navigationBarHeight: Int by mutableStateOf(50)

    var cutoutAreaColorDialogStatus: Boolean by mutableStateOf(false)
    var cutoutAreaColor: String by mutableStateOf("")

    var screenOrientationExpanded: Boolean by mutableStateOf(false)
    var screenOrientationType: Int by mutableStateOf(0)

    var openWebViewDialogStatus: Boolean by mutableStateOf(false)
    var openColorInputInvalidAlertStatus: Boolean by mutableStateOf(false)

    fun openWebView(activity: Activity, urlString: String) {
        val orientationType =
            when (screenOrientationType) {
                0 -> ScreenOrientation.PORTRAIT
                1 -> ScreenOrientation.LANDSCAPE
                2 -> ScreenOrientation.LANDSCAPE_REVERSE
                else -> ScreenOrientation.PORTRAIT
            }

        val configurationBuilder: GamebaseWebViewConfiguration.Builder =
            GamebaseWebViewConfiguration.Builder().setTitleText(navigationBarTitle)
                .setNavigationBarColor(Color.parseColor(navigationBarColor))
                .setNavigationBarHeight(navigationBarHeight)
                .setBackButtonVisible(navigationBarBackButtonStatus)
                .setNavigationBarVisible(navigationBarVisibleStatus)
                .setRenderOutsideSafeArea(renderOutSafeArea)
                .setScreenOrientation(orientationType)

        if (cutoutAreaColor != "") {
            configurationBuilder.setCutoutAreaColor(Color.parseColor(cutoutAreaColor))
        }
        val configuration: GamebaseWebViewConfiguration = configurationBuilder.build()
        showWebView(
            activity = activity,
            url = urlString,
            configuration = configuration,
            onCloseCallback = { },
            schemeList = null,
            onEvent = { data, exception ->
                if (isSuccess(exception)) {
                    Log.d(TAG, data.toString())
                } else {
                    val failedTitle: String =
                        GamebaseApplication.instance.applicationContext.getString(R.string.failed)
                    showAlert(activity, failedTitle, exception.toJsonString())
                }
            })
    }
}