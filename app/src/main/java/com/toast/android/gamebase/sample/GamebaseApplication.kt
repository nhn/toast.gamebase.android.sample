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

        // 앱이 실행되자마자 발생하는 크래시 로그도 빠짐없이 전송하려면 Application.onCreate()에서 NHN Cloud Logger를 초기화해야 합니다.
        if (getAppKey().isNotEmpty()) {
            initializeNhnCloudLogger(applicationContext)
            setNhnCloudLoggerListener()
        }
    }
}