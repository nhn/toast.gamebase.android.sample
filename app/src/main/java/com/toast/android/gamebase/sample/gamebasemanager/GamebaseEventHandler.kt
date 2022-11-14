package com.toast.android.gamebase.sample.gamebasemanager

import android.app.Activity
import android.util.Log
import com.toast.android.gamebase.Gamebase
import com.toast.android.gamebase.auth.data.AuthToken
import com.toast.android.gamebase.base.GamebaseError
import com.toast.android.gamebase.base.GamebaseException
import com.toast.android.gamebase.base.NetworkManager
import com.toast.android.gamebase.base.purchase.PurchasableReceipt
import com.toast.android.gamebase.event.GamebaseEventCategory
import com.toast.android.gamebase.event.GamebaseEventHandler
import com.toast.android.gamebase.event.data.GamebaseEventLoggedOutData
import com.toast.android.gamebase.event.data.GamebaseEventMessage
import com.toast.android.gamebase.event.data.GamebaseEventObserverData
import com.toast.android.gamebase.event.data.GamebaseEventServerPushData
import com.toast.android.gamebase.event.data.PushAction
import com.toast.android.gamebase.event.data.PushMessage
import com.toast.android.gamebase.launching.data.LaunchingStatus
import com.toast.android.gamebase.sample.util.printPushAction
import com.toast.android.gamebase.sample.util.printPushClickMessage
import com.toast.android.gamebase.sample.util.printWhat
import org.json.JSONObject

private var mGamebaseEventHandler: GamebaseEventHandler? = null

// Event Handler
fun addGamebaseEventHandler(activity: Activity) {
    if (mGamebaseEventHandler != null) {
        Gamebase.removeEventHandler(mGamebaseEventHandler)
    }
    mGamebaseEventHandler =
        GamebaseEventHandler { message ->
            when(message.category) {
                GamebaseEventCategory.LOGGED_OUT -> {
                    tryLoginAgain(activity, message)
                }
                GamebaseEventCategory.SERVER_PUSH_APP_KICKOUT_MESSAGE_RECEIVED,
                GamebaseEventCategory.SERVER_PUSH_APP_KICKOUT,
                GamebaseEventCategory.SERVER_PUSH_TRANSFER_KICKOUT -> {
                    GamebaseEventServerPushData.from(message.data)?.let {
                        processServerPush(activity, message.category, it)
                    }
                }
                GamebaseEventCategory.OBSERVER_LAUNCHING,
                GamebaseEventCategory.OBSERVER_HEARTBEAT,
                GamebaseEventCategory.OBSERVER_NETWORK -> {
                    GamebaseEventObserverData.from(message.data)?.let {
                        processObserver(activity, message.category, it)
                    }
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

private fun processServerPush(
    activity: Activity,
    category: String,
    data: GamebaseEventServerPushData
) {
    Log.d(TAG, "[GamebaseEventHandler] processServerPush")
    Log.i(TAG, "[GamebaseEventHandler] category : $category")
    Log.i(TAG, "[GamebaseEventHandler] serverPushData : $data")
    Log.d(TAG, "--------------------------------------")
    if (category == GamebaseEventCategory.SERVER_PUSH_APP_KICKOUT_MESSAGE_RECEIVED) {
        // Currently, the kickout pop-up is displayed.
        // If your game is running, stop it.
        // pauseGame()
    } else if (category == GamebaseEventCategory.SERVER_PUSH_APP_KICKOUT) {
        // Kicked out from Gamebase server.(Maintenance, banned or etc..)
        // And the game user closes the kickout pop-up.
        // Return to title and initialize Gamebase again.
        // returnToTitle(activity)
    } else if (category == GamebaseEventCategory.SERVER_PUSH_TRANSFER_KICKOUT) {
        // If the user wants to move the guest account to another device,
        // if the account transfer is successful,
        // the login of the previous device is released,
        // so go back to the title and try to log in again.
        // returnToTitle(activity)
    }
}

private fun tryLoginAgain(activity: Activity, message: GamebaseEventMessage) {
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

// Observer
private fun processObserver(
    activity: Activity,
    category: String,
    data: GamebaseEventObserverData
) {
    Log.d(TAG, "[GamebaseEventHandler] processObserver")
    Log.i(TAG, "[GamebaseEventHandler] category : $category")
    Log.i(TAG, "[GamebaseEventHandler] observerData : $data")
    Log.d(TAG, "--------------------------------------")

    if (category == GamebaseEventCategory.OBSERVER_LAUNCHING) {
        val launchingStatusCode = data.code
        val messageString = data.message
        Log.d(TAG, "Update launching status to $launchingStatusCode, $messageString")
        var canPlay = true
        var errorLog = ""
        when (launchingStatusCode) {
            LaunchingStatus.IN_SERVICE -> {}
            LaunchingStatus.RECOMMEND_UPDATE ->
                Log.d(TAG, "There is a new version of this application.")
            LaunchingStatus.IN_SERVICE_BY_QA_WHITE_LIST,
            LaunchingStatus.IN_TEST,
            LaunchingStatus.IN_REVIEW,
            LaunchingStatus.IN_BETA ->
                Log.d(TAG, "You logged in because you are developer.")
            LaunchingStatus.REQUIRE_UPDATE -> {
                canPlay = false
                errorLog = "You have to update this application."
            }
            LaunchingStatus.BLOCKED_USER -> {
                canPlay = false
                errorLog = "You are blocked user!"
            }
            LaunchingStatus.TERMINATED_SERVICE -> {
                canPlay = false
                errorLog = "Game is closed!"
            }
            LaunchingStatus.INSPECTING_SERVICE, LaunchingStatus.INSPECTING_ALL_SERVICES -> {
                canPlay = false
                errorLog = "Under maintenance."
            }
            LaunchingStatus.INTERNAL_SERVER_ERROR -> {
                canPlay = false
                errorLog = "Unknown internal error."
            }
            else -> {
                canPlay = false
                errorLog = "Unknown internal error."
            }
        }
        if (canPlay) {
            // Now, user can play the game.
            Log.d(TAG, "Changed Status : You can play game, now!")
        } else {
            // User cannot play the game.
            Log.e(TAG, "Changed Status : $errorLog")
            // returnToTitle(activity)
        }
    } else if (category == GamebaseEventCategory.OBSERVER_HEARTBEAT) {
        val errorCode = data.code
        Log.d(TAG, "Heartbeat changing : $data")
        when (errorCode) {
            GamebaseError.INVALID_MEMBER -> {}
            GamebaseError.BANNED_MEMBER -> {}
        }
    } else if (category == GamebaseEventCategory.OBSERVER_NETWORK) {
        val networkTypeCode = data.code
        Log.d(TAG, "Network changing : $data")

        // You can check the changed network status in here.
        if (networkTypeCode == NetworkManager.TYPE_NOT) {
            // Network disconnected.
        } else {
            // Network connected.
        }
    }
}
