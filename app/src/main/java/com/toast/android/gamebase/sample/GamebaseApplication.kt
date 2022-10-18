package com.toast.android.gamebase.sample

import android.app.Application
import android.content.Context

const val TAG = "GamebaseApplication"

class GamebaseApplication : Application() {
    init {
        instance = this
    }

    companion object {
        lateinit var instance: GamebaseApplication
        fun ApplicationContext(): Context {
            return instance.applicationContext
        }
    }

    override fun onCreate() {
        super.onCreate()
    }
}