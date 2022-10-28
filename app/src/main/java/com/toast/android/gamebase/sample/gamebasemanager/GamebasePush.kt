package com.toast.android.gamebase.sample.gamebasemanager

import android.app.Activity
import android.util.Log
import com.toast.android.gamebase.Gamebase
import com.toast.android.gamebase.GamebaseCallback
import com.toast.android.gamebase.GamebaseDataCallback
import com.toast.android.gamebase.base.GamebaseException
import com.toast.android.gamebase.base.push.PushConfiguration
import com.toast.android.gamebase.base.push.data.GamebaseNotificationOptions
import com.toast.android.gamebase.base.push.data.GamebasePushTokenInfo
import com.toast.android.gamebase.sample.util.printQueryTokenInfo

fun registerPush(
    activity: Activity,
    configuration: PushConfiguration,
    callback: GamebaseCallback?
) {
    Gamebase.Push.registerPush(
        activity,
        configuration
    ) { exception: GamebaseException? ->
        if (Gamebase.isSuccess(exception)) {
            Log.v(TAG, "Register push succeeded")
        } else {
            Log.e(TAG, "Register push failed : $exception")
        }
        callback?.onCallback(exception)
    }
}

fun registerPush(
    activity: Activity,
    configuration: PushConfiguration,
    notificationOptions: GamebaseNotificationOptions,
    callback: GamebaseCallback?
) {
    Gamebase.Push.registerPush(activity, configuration, notificationOptions) { exception ->
        if (Gamebase.isSuccess(exception)) {
            Log.v(TAG, "Register push succeeded")
        } else {
            Log.e(TAG, "Register push failed : $exception")
        }
        callback?.onCallback(exception)
    }
}

fun getNotificationOptions(activity: Activity): GamebaseNotificationOptions =
    Gamebase.Push.getNotificationOptions(activity)

fun queryTokenInfo(
    activity: Activity,
    callback: GamebaseDataCallback<GamebasePushTokenInfo>?
) {
    Gamebase.Push.queryTokenInfo(
        activity
    ) { tokenInfo, exception ->
        if (Gamebase.isSuccess(exception)) {
            printQueryTokenInfo(TAG, tokenInfo)
        } else {
            Log.e(TAG, "QueryTokenInfo failed : $exception")
        }
        callback?.onCallback(tokenInfo, exception)
    }
}