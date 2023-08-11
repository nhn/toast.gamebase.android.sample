package com.toast.android.gamebase.sample

import androidx.multidex.MultiDexApplication
import com.toast.android.gamebase.sample.gamebase_manager.getAppKey
import com.toast.android.gamebase.sample.gamebase_manager.initializeNhnCloudLogger
import com.toast.android.gamebase.sample.gamebase_manager.setNhnCloudLoggerListener

const val TAG = "GamebaseApplication"

class GamebaseApplication : MultiDexApplication() {
    companion object {
        lateinit var instance: GamebaseApplication
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this

        // To send crash logs that occur as soon as the app starts, you must initialize the NHN Cloud Logger in Application.onCreate().
        if (getAppKey().isNotEmpty()) {
            initializeNhnCloudLogger(applicationContext)
            setNhnCloudLoggerListener()
        }
    }
}