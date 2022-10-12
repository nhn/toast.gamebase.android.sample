package com.nhn.android.gamebase.sample.util

import android.content.Context

private val name = "SamplePreference"
private val wrongIntValue = -1

fun putIntInPreference(context : Context, key: String, value: Int) {
    val mPreferenceEditor = context.getSharedPreferences(name, Context.MODE_PRIVATE).edit()
    mPreferenceEditor.putInt(key, value)
    mPreferenceEditor.apply()
}

fun getIntInPreference(context : Context, key: String): Int {
    val mSharedPreference = context.getSharedPreferences(name, Context.MODE_PRIVATE)
    return mSharedPreference.getInt(key, wrongIntValue)
}



