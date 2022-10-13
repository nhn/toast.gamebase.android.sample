package com.toast.android.gamebase.sample.util

import android.util.Log
import com.toast.android.gamebase.sample.GamebaseManager
import com.toast.android.gamebase.Gamebase
import com.toast.android.gamebase.auth.data.AuthToken
import com.toast.android.gamebase.auth.data.BanInfo
import com.toast.android.gamebase.base.GamebaseException
import com.toast.android.gamebase.base.auth.AuthProviderProfile
import com.toast.android.gamebase.base.purchase.PurchasableReceipt
import com.toast.android.gamebase.event.data.GamebaseEventMessage
import com.toast.android.gamebase.event.data.PushAction
import com.toast.android.gamebase.event.data.PushMessage

fun printLoginSuccess(TAG: String, authToken: AuthToken) {
    Log.i(TAG, "Gamebase User Id : ${authToken.userId}")
    Log.i(TAG, "Gamebase Access Token : ${authToken.accessToken}")
    Log.i(TAG, "LastLoggedInProvider : ${Gamebase.getLastLoggedInProvider()}")
    Log.d(TAG, "--------------------------------------")
}

fun printLoginWithIdpSuccess(TAG: String, provider: String) {
    val profileAsString = Gamebase.getAuthProviderProfile(provider)?.toJsonString() ?: "null"
    Log.d(TAG, "Login with IdP($provider) Success")
    Log.i(TAG, "$provider User Id : ${Gamebase.getAuthProviderUserID(provider)}")
    Log.i(TAG, "$provider Access Token : ${Gamebase.getAuthProviderAccessToken(provider)}")
    Log.i(TAG, "$provider Profile : $profileAsString")
    Log.d(TAG, "--------------------------------------")
}

fun printLoginError(TAG: String, exception: GamebaseException) {
    Log.w(TAG, "Login Exception")
    Log.w(TAG, "Login Exception - Domain : ${exception.domain}")
    Log.w(TAG, "Login Exception - Message : ${exception.message}")
    Log.w(TAG, "Login Exception - Code : ${exception.code}")
    Log.w(TAG, "Login Exception - Detail : $exception")
    Log.w(TAG, "--------------------------------------")
}

fun printBanInfo(TAG: String, banInfo: BanInfo) {
    Log.d(TAG, "Ban Info")
    Log.d(TAG, "Ban Info - User ID : ${banInfo.userId}")
    Log.d(TAG, "Ban Info - Ban Type : ${banInfo.banType}")
    Log.d(TAG, "Ban Info - Message : ${banInfo.message}")
    Log.d(TAG, "Ban Info - Begin Date : ${banInfo.beginDate}")
    Log.d(TAG, "Ban Info - End Date : ${banInfo.endDate}")
    Log.d(TAG, "--------------------------------------")
}

fun printWhat(TAG: String, category: String, receipt: PurchasableReceipt) {
    // If the user got item by 'Promotion Code',
    // this event will be occurred.
    Log.i(TAG, "[GamebaseEventHandler] category : $category")
    Log.i(TAG, "[GamebaseEventHandler] PurchasableReceipt : $receipt")
    Log.d(TAG, "--------------------------------------")
}

fun printPushClickMessage(TAG: String, category: String, pushMessage: PushMessage) {
    // When you clicked push message.
    Log.i(TAG, "[GamebaseEventHandler] category : $category")
    Log.i(TAG, "[GamebaseEventHandler] PushMessage(clickedMessage) : $pushMessage")
    Log.d(TAG, "--------------------------------------")
}

fun printPushAction(TAG: String, category: String, pushAction: PushAction) {
    // When you clicked action button by 'Rich Message'.
    Log.i(TAG, "[GamebaseEventHandler] category : $category")
    Log.i(TAG, "[GamebaseEventHandler] PushAction : $pushAction")
    Log.d(TAG, "--------------------------------------")
}