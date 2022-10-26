package com.toast.android.gamebase.sample.gamebasemanager

import android.app.Activity
import android.graphics.Color
import android.util.Log
import com.toast.android.gamebase.Gamebase
import com.toast.android.gamebase.GamebaseCallback
import com.toast.android.gamebase.GamebaseDataCallback
import com.toast.android.gamebase.GamebaseWebViewConfiguration
import com.toast.android.gamebase.GamebaseWebViewStyle
import com.toast.android.gamebase.base.GamebaseError
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
    callback: GamebaseDataCallback<GamebaseDataContainer?>?
) {
    Gamebase.Terms.showTermsView(
        activity,
    ) { container, exception -> callback?.onCallback(container, exception) }
}

// You can show terms view force with setting setForceShow(true) configuration
fun showTermsViewForceShow(
    activity: Activity,
    callback: GamebaseDataCallback<GamebaseDataContainer?>?
) {
    Gamebase.Terms.showTermsView(
        activity,
        GamebaseTermsConfiguration.newBuilder().setForceShow(true).build(),
    ) { container, exception -> callback?.onCallback(container, exception) }
}

fun queryTerms(
    activity: Activity,
    callback: GamebaseDataCallback<GamebaseQueryTermsResult>?
) {
    Gamebase.Terms.queryTerms(
        activity
    ) { gamebaseQueryTermsResult, exception ->
        if (Gamebase.isSuccess(exception)) {
            // Succeeded.
            val termsSeq = gamebaseQueryTermsResult.termsSeq
            val termsVersion = gamebaseQueryTermsResult.termsVersion
            val termsCountryType = gamebaseQueryTermsResult.termsCountryType
            val contents = gamebaseQueryTermsResult.contents
        } else if (exception.code == GamebaseError.UI_TERMS_NOT_EXIST_FOR_DEVICE_COUNTRY) {
            // Another country device.
            // Pass the 'terms and conditions' step.
        } else {
            // Failed.
        }
        callback?.onCallback(gamebaseQueryTermsResult, exception)
    }
}

fun updateTerms(
    activity: Activity,
    configuration: GamebaseUpdateTermsConfiguration,
    callback: GamebaseCallback?
) {
    Gamebase.Terms.updateTerms(
        activity, configuration
    ) { exception -> callback?.onCallback(exception) }
}

fun updateTermsExample(
    activity: Activity,
    callback: GamebaseCallback?
) {
    queryTerms(activity,
        GamebaseDataCallback<GamebaseQueryTermsResult>() { (termsSeq, termsVersion, _, contents1), queryTermsException ->
            val contents: MutableList<GamebaseTermsContent> = ArrayList()
            for (detail in contents1) {
                val content = GamebaseTermsContent.from(detail)
                if (content != null) {
                    // Change 'agreed' value for test.
                    content.agreed = !content.agreed
                    contents.add(content)
                }
            }
            val configuration =
                GamebaseUpdateTermsConfiguration.newBuilder(termsSeq, termsVersion, contents)
                    .build()
            updateTerms(
                activity, configuration
            ) { updateTermsException -> callback?.onCallback(updateTermsException) }
        })
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
