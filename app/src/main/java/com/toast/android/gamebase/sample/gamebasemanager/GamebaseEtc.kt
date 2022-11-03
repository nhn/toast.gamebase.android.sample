package com.toast.android.gamebase.sample.gamebasemanager

import android.app.Activity
import android.util.Log
import com.toast.android.gamebase.Gamebase
import com.toast.android.gamebase.auth.data.AuthToken
import com.toast.android.gamebase.base.GamebaseError
import com.toast.android.gamebase.base.GamebaseException
import com.toast.android.gamebase.base.purchase.PurchasableReceipt
import com.toast.android.gamebase.contact.ContactConfiguration
import com.toast.android.gamebase.event.GamebaseEventCategory
import com.toast.android.gamebase.event.GamebaseEventHandler
import com.toast.android.gamebase.event.data.GamebaseEventLoggedOutData
import com.toast.android.gamebase.event.data.GamebaseEventMessage
import com.toast.android.gamebase.event.data.PushAction
import com.toast.android.gamebase.event.data.PushMessage
import com.toast.android.gamebase.sample.util.printPushAction
import com.toast.android.gamebase.sample.util.printPushClickMessage
import com.toast.android.gamebase.sample.util.printWhat
import org.json.JSONObject

const val TAG = "GamebaseManager"
private var mGamebaseEventHandler: GamebaseEventHandler? = null

////////////////////////////////////////////////////////////////////////////////
//
// Common
//
////////////////////////////////////////////////////////////////////////////////
fun isSuccess(exception: GamebaseException?): Boolean =
    Gamebase.isSuccess(exception)

fun showAlert(activity: Activity, title: String, message: String) {
    Gamebase.Util.showAlert(activity, title, message)
}

fun showToast(activity: Activity, message: String, duration: Int) {
    Gamebase.Util.showToast(activity, message, duration)
}

internal fun isNetworkError(exception: GamebaseException): Boolean {
    val errorCode = exception.code
    return errorCode == GamebaseError.SOCKET_ERROR ||
            errorCode == GamebaseError.SOCKET_RESPONSE_TIMEOUT
}

internal fun isBannedUser(exception: GamebaseException): Boolean {
    return exception.code == GamebaseError.BANNED_MEMBER
}

////////////////////////////////////////////////////////////////////////////////
//
// Event Handler
//
////////////////////////////////////////////////////////////////////////////////
// TODO: 동작 테스트 필요
fun addGamebaseEventHandler(activity: Activity) {
    if (mGamebaseEventHandler != null) {
        Gamebase.removeEventHandler(mGamebaseEventHandler)
    }
    mGamebaseEventHandler =
        GamebaseEventHandler { message: GamebaseEventMessage ->
            when (message.category) {
                GamebaseEventCategory.LOGGED_OUT -> {
                    onLoggedOut(activity, message)
                }
                GamebaseEventCategory.SERVER_PUSH_APP_KICKOUT_MESSAGE_RECEIVED,
                GamebaseEventCategory.SERVER_PUSH_APP_KICKOUT,
                GamebaseEventCategory.SERVER_PUSH_TRANSFER_KICKOUT -> {
                    //TODO: processServerPush
                }
                GamebaseEventCategory.OBSERVER_LAUNCHING,
                GamebaseEventCategory.OBSERVER_HEARTBEAT,
                GamebaseEventCategory.OBSERVER_NETWORK -> {
                    //TODO: processObserver
                }
                GamebaseEventCategory.PURCHASE_UPDATED -> {
                    PurchasableReceipt.from(message.data)?.let {
                        printWhat(TAG, message.category, it)
                    }
                }
                GamebaseEventCategory.PUSH_RECEIVED_MESSAGE -> {
                    onPushReceiveMessage(message)
                }
                GamebaseEventCategory.PUSH_CLICK_MESSAGE -> {
                    PushMessage.from(message.data)?.let {
                        printPushClickMessage(TAG, message.category, it)
                    }
                }
                GamebaseEventCategory.PUSH_CLICK_ACTION -> {
                    PushAction.from(message.data)?.let {
                        printPushAction(TAG, message.category, it)
                    }
                }
                else -> {}
            }
        }
    Gamebase.addEventHandler(mGamebaseEventHandler)
}

private fun onLoggedOut(activity: Activity, message: GamebaseEventMessage) {
    val loggedOutData = GamebaseEventLoggedOutData.from(message.data)
    if (loggedOutData != null) {
        Log.i(TAG, "[GamebaseEventHandler] category : " + message.category)
        Log.i(TAG, "[GamebaseEventHandler] loggedOutData : $loggedOutData")
        Log.i(TAG, "[GamebaseEventHandler] Process Login again.")
        Log.d(TAG, "--------------------------------------")
        // There was a problem with the access token.
        // Call login again.
        Gamebase.login(
            activity, Gamebase.getLastLoggedInProvider()
        ) { _: AuthToken?, _: GamebaseException? -> }
    }
}

private fun onPushReceiveMessage(message: GamebaseEventMessage) {
    val pushMessage = PushMessage.from(message.data)
    if (pushMessage != null) {
        // When you received push message.
        Log.i(TAG, "[GamebaseEventHandler] category : " + message.category)
        Log.i(TAG, "[GamebaseEventHandler] PushMessage : $pushMessage")
        Log.d(TAG, "--------------------------------------")
        try {
            val json = JSONObject(pushMessage.extras)
            // There is 'isForeground' information.
            val isForeground = json.getBoolean("isForeground")
            // You can get your custom key.
            if (json.has("YourCustomKey")) {
                val customValue = json["YourCustomKey"]
            }
        } catch (ignored: Exception) {
        }
    }
}

////////////////////////////////////////////////////////////////////////////////
//
// Contact
//
////////////////////////////////////////////////////////////////////////////////
fun openContact(
    activity: Activity,
    userName: String?,
    callback: (() -> Unit)?
) {
    val builder = ContactConfiguration.newBuilder()
    if (userName != null) {
        builder.setUserName(userName)
    }
    Gamebase.Contact.openContact(
        activity, builder.build()
    ) {
        if (callback != null) {
            callback()
        }
    }
}