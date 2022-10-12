package com.nhn.android.gamebase.sample.util

import android.content.Context

private val name = "SamplePreference"

fun putIntInPreference(context : Context, key: String, value: Int) {
    val mPreferenceEditor = context.getSharedPreferences(name, Context.MODE_PRIVATE).edit()
    mPreferenceEditor.putInt(key, value)
    mPreferenceEditor.apply()
}

fun getIntInPreference(context : Context, key: String, defaultValue : Int): Int {
    val mSharedPreference = context.getSharedPreferences(name, Context.MODE_PRIVATE)
    return mSharedPreference.getInt(key, defaultValue)
}