package com.toast.android.gamebase.sample

import androidx.multidex.MultiDexApplication
import com.toast.android.gamebase.sample.gamebase_manager.getAppKey
import com.toast.android.gamebase.sample.gamebase_manager.initializeNhnCloudLogger
import com.toast.android.gamebase.sample.gamebase_manager.setNhnCloudLoggerListener

class GamebaseApplication : MultiDexApplication() {
    companion object {
        lateinit var instance: GamebaseApplication
            private set
    }

    // Application의 재생성을 탐지하기 위해 Application이 생성될 때 launchedTime 저장
    val launchedTime = System.currentTimeMillis()

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