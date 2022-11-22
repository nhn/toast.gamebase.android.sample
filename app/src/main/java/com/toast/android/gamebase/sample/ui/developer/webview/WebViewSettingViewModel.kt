/*
 * © NHN Corp. All rights reserved.
 * NHN Corp. PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.toast.android.gamebase.sample.ui.developer.webview

import android.app.Activity
import android.graphics.Color
import android.util.Log
import androidx.compose.runtime.mutableStateOf
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
    val navigationBarVisibleStatus = mutableStateOf(false)
    val navigationBarBackButtonStatus = mutableStateOf(false)
    val navigationBarTitleDialogStatus = mutableStateOf(false)
    val navigationBarTitle = mutableStateOf("")
    val navigationBarColorDialogStatus = mutableStateOf(false)
    val navigationBarColor = mutableStateOf("#80000000")
    val navigationBarHeightDialogStatus = mutableStateOf(false)
    val navigationBarHeight = mutableStateOf(50)
    val screenOrientationExpanded = mutableStateOf(false)
    val screenOrientationType = mutableStateOf(0)
    val openWebViewDialogStatus = mutableStateOf(false)

    fun openWebView(activity: Activity, urlString: String) {
        val orientationType =
            when (screenOrientationType.value) {
                0 -> ScreenOrientation.PORTRAIT
                1 -> ScreenOrientation.LANDSCAPE
                2 -> ScreenOrientation.LANDSCAPE_REVERSE
                else -> ScreenOrientation.PORTRAIT
            }

        val configuration: GamebaseWebViewConfiguration =
            GamebaseWebViewConfiguration.Builder().setTitleText(navigationBarTitle.value)
                .setNavigationBarColor(Color.parseColor(navigationBarColor.value))
                .setNavigationBarHeight(navigationBarHeight.value)
                .setBackButtonVisible(navigationBarBackButtonStatus.value)
                .setNavigationBarVisible(navigationBarVisibleStatus.value)
                .setScreenOrientation(orientationType)
                .build()
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