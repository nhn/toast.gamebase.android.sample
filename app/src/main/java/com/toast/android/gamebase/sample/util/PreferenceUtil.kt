/*
 * © NHN Corp. All rights reserved.
 * NHN Corp. PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.toast.android.gamebase.sample.util

import android.content.Context
import com.toast.android.gamebase.base.log.Logger
import com.toast.android.gamebase.base.push.PushConfiguration
import com.toast.android.gamebase.sample.GamebaseApplication

private const val PREFERENCE_NAME = "gamebase.sample.app"
private const val PREF_KEY_PUSH_CONFIGURATION = "gamebase.sample.pref.push.configuration"

fun putIntInPreference(context : Context, key: String, value: Int) {
    val mPreferenceEditor = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE).edit()
    mPreferenceEditor.putInt(key, value)
    mPreferenceEditor.apply()
}

fun getIntInPreference(context : Context, key: String, defaultValue : Int): Int {
    val mSharedPreference = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
    return mSharedPreference.getInt(key, defaultValue)
}

fun savePushConfiguration(
    pushConfiguration: PushConfiguration
) {
    val applicationContext = GamebaseApplication.instance.applicationContext
    val pref = applicationContext.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
    val editor = pref.edit()
    editor.putString(PREF_KEY_PUSH_CONFIGURATION, pushConfiguration.toJsonString())
    editor.apply()
}

fun loadPushConfiguration(): PushConfiguration? {
    val applicationContext = GamebaseApplication.instance.applicationContext
    val pref = applicationContext.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
    if (pref.contains(PREF_KEY_PUSH_CONFIGURATION)) {
        val pushConfigStr = pref.getString(PREF_KEY_PUSH_CONFIGURATION, "{}")
        return try {
            PushConfiguration.from(pushConfigStr)
        } catch (e: Exception) {
            e.printStackTrace()
            Logger.w(
                "PushConfiguration",
                "PushConfiguration.from($pushConfigStr) : ${e.message}")
            null
        }
    }
    return null
}
