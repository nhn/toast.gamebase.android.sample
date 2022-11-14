package com.toast.android.gamebase.sample.gamebasemanager

import android.app.Activity
import android.content.DialogInterface
import android.util.Log
import com.toast.android.gamebase.Gamebase
import com.toast.android.gamebase.analytics.data.GameUserData
import com.toast.android.gamebase.analytics.data.LevelUpData
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

// Common
fun isSuccess(exception: GamebaseException?): Boolean =
    Gamebase.isSuccess(exception)

fun showAlert(activity: Activity, title: String, message: String) {
    Gamebase.Util.showAlert(activity, title, message)
}

fun showAlert(activity: Activity, title: String, message: String, callback: DialogInterface.OnClickListener) {
    Gamebase.Util.showAlert(activity, title, message, callback)
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

// Event Handler
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

// Analytics
fun setGameUserData(userLevel: Int, channelId: String?, characterId: String?, classId: String?) {
    val gameUserData: GameUserData = GameUserData(userLevel).apply {
        this.channelId = channelId
        this.characterId = characterId
        this.classId = classId
    }

    Gamebase.Analytics.setGameUserData(gameUserData)
}

fun onLevelUp(userLevel: Int, levelUpTime: Long) {
    val levelUpData: LevelUpData = LevelUpData(userLevel, levelUpTime)

    Gamebase.Analytics.traceLevelUp(levelUpData)
}

// Contact
fun getContactUrl(
    configuration: ContactConfiguration? = null,
    onClosedCallback: ((String, GamebaseException?) -> Unit)
) {
    if (configuration == null) {
        Gamebase.Contact.requestContactURL(onClosedCallback)
    } else {
        Gamebase.Contact.requestContactURL(configuration, onClosedCallback)
    }
}

fun openContact(
    activity: Activity,
    configuration: ContactConfiguration?,
    onClosedCallback: ((GamebaseException?) -> Unit)
) {
    if (configuration != null) {
        Gamebase.Contact.openContact(activity, configuration, onClosedCallback)
    } else {
        Gamebase.Contact.openContact(activity, onClosedCallback)
    }
}

fun getDeviceLanguage(): String {
    return Gamebase.getDeviceLanguageCode()
}

fun getDisplayLanguage(): String {
    return Gamebase.getDisplayLanguageCode()
}

fun getCountryCodeOfUSIM(): String {
    return Gamebase.getCountryCodeOfUSIM()
}

fun getCountryCodeOfDevice(): String {
    return Gamebase.getCountryCodeOfDevice()
}

/*
* USIM, 단말기 언어 설정의 순서로 국가 코드를 확인하여 리턴합니다.
* getCountryCode API는 다음 순서로 동작합니다.
* USIM에 기록된 국가 코드를 확인해 보고 값이 존재한다면 추가적인 체크 없이 그대로 리턴합니다.
* USIM 국가 코드가 빈 값이라면 단말기 국가 코드를 확인해 보고 값이 존재한다면 추가적인 체크 없이 그대로 리턴합니다.
* USIM, 단말기 국가 코드가 모두 빈 값이라면 'ZZ'를 리턴합니다.
* */
fun getIntegratedCountryCode(): String {
    return Gamebase.getCountryCode()
}
