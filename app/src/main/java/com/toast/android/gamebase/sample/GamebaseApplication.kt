package com.toast.android.gamebase.sample

import androidx.multidex.MultiDexApplication

const val TAG = "GamebaseApplication"

class GamebaseApplication : MultiDexApplication() {
    companion object {
        lateinit var instance: GamebaseApplication
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}