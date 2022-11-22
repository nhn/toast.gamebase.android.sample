package com.toast.android.gamebase.sample.gamebase_manager

import android.app.Activity
import android.content.DialogInterface
import android.util.Log
import com.toast.android.gamebase.Gamebase
import com.toast.android.gamebase.GamebaseCallback
import com.toast.android.gamebase.GamebaseDataCallback
import com.toast.android.gamebase.GamebaseWebViewConfiguration
import com.toast.android.gamebase.GamebaseWebViewStyle
import com.toast.android.gamebase.base.data.GamebaseDataContainer
import com.toast.android.gamebase.imagenotice.ImageNoticeConfiguration
import com.toast.android.gamebase.terms.GamebaseTermsConfiguration
import com.toast.android.gamebase.terms.data.GamebaseQueryTermsResult
import com.toast.android.gamebase.terms.data.GamebaseTermsContent
import com.toast.android.gamebase.terms.data.GamebaseUpdateTermsConfiguration

// UI
// https://docs.toast.com/en/Game/Gamebase/en/aos-ui/

fun showImageNotices(activity: Activity, onCloseCallback: GamebaseCallback? = null) {
    val configuration = ImageNoticeConfiguration.newBuilder()
        .build()
    Gamebase.ImageNotice.showImageNotices(
        activity,
        configuration,
        { exception -> onCloseCallback?.onCallback(exception) },
        { payload, exception ->
            if (Gamebase.isSuccess(exception)) {
                Log.i(TAG, "Clicked Image Notice Payload: $payload")
                if (payload.equals("mygame://some_custom_scheme", ignoreCase = true)) {
                    // Do something with your custom scheme
                } else {
                    showAlert(activity, TAG, exception.toJsonString())
                }
            }
        })
}

fun showImageNotices(
    activity: Activity,
    configuration: ImageNoticeConfiguration,
    onCloseCallback: GamebaseCallback? = null
) {
    Gamebase.ImageNotice.showImageNotices(
        activity,
        configuration,
        { exception -> onCloseCallback?.onCallback(exception) },
        { payload, exception ->
            if (Gamebase.isSuccess(exception)) {
                Log.i(TAG, "Clicked Image Notice Payload: $payload")
                if (payload.equals("mygame://some_custom_scheme", ignoreCase = true)) {
                    // Do something with your custom scheme
                }
                showAlert(activity, "Payload", payload)
            } else {
                showAlert(activity, TAG, exception.toJsonString())
            }
        })
}

fun closeImageNotices(activity: Activity?) {
    Gamebase.ImageNotice.closeImageNotices(activity!!)
}

// Terms
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

// WebView
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

fun showAlert(activity: Activity, title: String, message: String) {
    Gamebase.Util.showAlert(activity, title, message)
}

fun showAlert(activity: Activity, title: String, message: String, callback: DialogInterface.OnClickListener) {
    Gamebase.Util.showAlert(activity, title, message, callback)
}

fun showToast(activity: Activity, message: String, duration: Int) {
    Gamebase.Util.showToast(activity, message, duration)
}
