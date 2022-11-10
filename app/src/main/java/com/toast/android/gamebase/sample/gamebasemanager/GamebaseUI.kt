package com.toast.android.gamebase.sample.gamebasemanager

import android.app.Activity
import android.graphics.Color
import android.util.Log
import com.toast.android.gamebase.Gamebase
import com.toast.android.gamebase.GamebaseCallback
import com.toast.android.gamebase.GamebaseDataCallback
import com.toast.android.gamebase.GamebaseWebViewConfiguration
import com.toast.android.gamebase.GamebaseWebViewStyle
import com.toast.android.gamebase.base.ScreenOrientation
import com.toast.android.gamebase.base.data.GamebaseDataContainer
import com.toast.android.gamebase.imagenotice.ImageNoticeConfiguration
import com.toast.android.gamebase.terms.GamebaseTermsConfiguration
import com.toast.android.gamebase.terms.data.GamebaseQueryTermsResult
import com.toast.android.gamebase.terms.data.GamebaseTermsContent
import com.toast.android.gamebase.terms.data.GamebaseUpdateTermsConfiguration

fun showImageNotices(activity: Activity, onCloseCallback: GamebaseCallback) {
    val configuration = ImageNoticeConfiguration.newBuilder()
        .build()
    Gamebase.ImageNotice.showImageNotices(
        activity,
        configuration,
        { exception -> onCloseCallback.onCallback(exception) },
        { payload, exception ->
            if (Gamebase.isSuccess(exception)) {
                Log.i(TAG, "Clicked Image Notice Payload: $payload")
                if (payload.equals("mygame://some_custom_scheme", ignoreCase = true)) {
                    // Do something with your custom scheme
                }
            }
        })
}

fun showImageNotices(
    activity: Activity,
    configuration: ImageNoticeConfiguration,
    onCloseCallback: GamebaseCallback
) {
    Gamebase.ImageNotice.showImageNotices(
        activity,
        configuration,
        { exception -> onCloseCallback.onCallback(exception) },
        { payload, exception ->
            if (Gamebase.isSuccess(exception)) {
                Log.i(TAG, "Clicked Image Notice Payload: $payload")
                if (payload.equals("mygame://some_custom_scheme", ignoreCase = true)) {
                    // Do something with your custom scheme
                }
            }
        })
}

fun closeImageNotices(activity: Activity?) {
    Gamebase.ImageNotice.closeImageNotices(activity!!)
}

fun imageNoticesExample(activity: Activity, onCloseCallback: GamebaseCallback) {
    val configuration = ImageNoticeConfiguration.newBuilder()
        .setBackgroundColor("#80000000")
        .setTimeout(5000L)
        .enableAutoCloseByCustomScheme(false)
        .build()
    Gamebase.ImageNotice.showImageNotices(
        activity, configuration,
        { exception -> onCloseCallback.onCallback(exception) }
    ) { payload, exception ->
        if (Gamebase.isSuccess(exception)) {
            Log.i(TAG, "Clicked Image Notice Payload: $payload")
            if (payload.equals("GO_TO_IN_GAME_STORE", ignoreCase = true)) {
                // Custom: go to game store
                closeImageNotices(activity)
            } else if (payload.equals("GO_TO_PUSH_SETTINGS", ignoreCase = true)) {
                // custom: go to push setting menu
                closeImageNotices(activity)
            }
        }
    }
}

fun showTermsView(
    activity: Activity,
    configuration: GamebaseTermsConfiguration? = null,
    callback: GamebaseDataCallback<GamebaseDataContainer?>?
) {
    if (configuration != null) {
        Gamebase.Terms.showTermsView(
            activity,
            configuration,
        ) { container, exception -> callback?.onCallback(container, exception) }
    } else {
        Gamebase.Terms.showTermsView(
            activity,
        ) { container, exception -> callback?.onCallback(container, exception) }
    }
}

fun queryTerms(
    activity: Activity,
    callback: GamebaseDataCallback<GamebaseQueryTermsResult>?
) {
    Gamebase.Terms.queryTerms(
        activity
    ) { gamebaseQueryTermsResult, exception ->
        callback?.onCallback(gamebaseQueryTermsResult, exception)
    }
}

fun updateTerms(
    activity: Activity,
    termsSeq: Int?,
    termsVersion: String?,
    contents: List<GamebaseTermsContent>?,
    callback: GamebaseCallback?
) {
    if (termsVersion == null || termsSeq == null || contents == null) {
        Log.e(TAG, "update Terms argument is null")
        return
    }
    val configuration =
        GamebaseUpdateTermsConfiguration
            .newBuilder(termsSeq, termsVersion, contents)
            .build()

    Gamebase.Terms.updateTerms(
        activity, configuration
    ) { updateTermsException ->
        callback?.onCallback(updateTermsException)
    }
}

fun showWebView(activity: Activity, urlString: String) {
    Gamebase.WebView.showWebView(activity, urlString)
}

fun showWebView(
    activity: Activity,
    url: String,
    configuration: GamebaseWebViewConfiguration?,
    onCloseCallback: GamebaseCallback?,
    schemeList: List<String>?,
    onEvent: GamebaseDataCallback<String?>?
) {
    Gamebase.WebView.showWebView(
        activity,
        url, configuration, onCloseCallback, schemeList, onEvent
    )
}

// Close currently displayed WebView.
fun closeWebView(activity: Activity) {
    Gamebase.WebView.closeWebView(activity)
}

// Open with External Web Browser.
fun openExternalBrowser(activity: Activity, urlString: String) {
    Gamebase.WebView.openWebBrowser(activity, urlString)
}

private fun webViewExample(
    activity: Activity,
    urlString: String,
    title: String
) {
    val configuration = GamebaseWebViewConfiguration.Builder()
        .setStyle(GamebaseWebViewStyle.BROWSER) // BROWSER, POPUP
        .setTitleText(title)
        .setScreenOrientation(ScreenOrientation.PORTRAIT) // PORTRAIT, LANDSCAPE, LANDSCAPE_SENSOR
        .setNavigationBarColor(Color.RED)
        .setNavigationBarHeight(40)
        .setBackButtonVisible(true) // Set Back Button Visible
        //                .setBackButtonImageResource(R.id.back_button)       // Set Back Button Image ID of resource
        //                .setCloseButtonImageResource(R.id.close_button)     // Set Close Button Image ID of resource
        .build()
    showWebView(activity, urlString, configuration, null, null, null)
}

private fun webViewExampleWithSchemeEvent(
    activity: Activity,
    urlString: String,
    title: String
) {
    val configuration = GamebaseWebViewConfiguration.Builder()
        .setStyle(GamebaseWebViewStyle.BROWSER) // BROWSER, POPUP
        .setTitleText(title)
        .build()
    val schemeList: MutableList<String> = java.util.ArrayList()
    schemeList.add("mygame://")
    schemeList.add("closemywebview://")
    showWebView(activity, urlString, configuration,
        {
            // When closed WebView, this callback will be called.
        },
        schemeList,
        { fullUrl, exception ->
            if (Gamebase.isSuccess(exception)) {
                if (fullUrl?.contains("mygame://") == true) {
                    // do something
                } else if (fullUrl?.contains("closemywebview://") == true) {
                    closeWebView(activity)
                }
            } else {
                showAlert(
                    activity,
                    "Exception from WebView Callback",
                    exception.toString())
            }
        })
}
