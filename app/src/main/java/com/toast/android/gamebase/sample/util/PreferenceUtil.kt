package com.toast.android.gamebase.sample.util

import android.content.Context

private const val PREFERENCE_NAME = "gamebase.sample.app"

fun putIntInPreference(context : Context, key: String, value: Int) {
    val mPreferenceEditor = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE).edit()
    mPreferenceEditor.putInt(key, value)
    mPreferenceEditor.apply()
}

fun getIntInPreference(context : Context, key: String, defaultValue : Int): Int {
    val mSharedPreference = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
    return mSharedPreference.getInt(key, defaultValue)
}