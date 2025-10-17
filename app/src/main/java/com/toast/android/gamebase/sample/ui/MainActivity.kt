package com.toast.android.gamebase.sample.ui

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.toast.android.gamebase.Gamebase
import com.toast.android.gamebase.sample.BuildConfig
import com.toast.android.gamebase.sample.GamebaseActivity
import com.toast.android.gamebase.sample.GamebaseApplication
import com.toast.android.gamebase.sample.ui.access_info.AccessInformationScreen
import com.toast.android.gamebase.sample.ui.navigation.SampleAppScreens
import com.toast.android.gamebase.sample.ui.theme.GamebaseSampleProjectTheme
import com.toast.android.gamebase.sample.util.getIntInPreference
import com.toast.android.gamebase.sample.util.putIntInPreference

class MainActivity : GamebaseActivity() {
    companion object {
        const val KEY_LAST_ACCESS_INFO_SHOWN_VERSION = "gamebase.sample.pref.access.info.shown.version"
        private const val APPLICATION_LAUNCHED_TIME = "applicationLaunchedTime"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Gamebase가 초기화 되었으면 Splash를 스킵하고 Login 화면부터 띄워준다.
        val startRoute = if (Gamebase.isInitialized()) SampleAppScreens.Login.route else SampleAppScreens.Splash.route

        var shouldShowAccessInformationScreen by mutableStateOf(true)

        if (getIntInPreference(applicationContext, KEY_LAST_ACCESS_INFO_SHOWN_VERSION, -1) == BuildConfig.VERSION_CODE) {
            shouldShowAccessInformationScreen = false
        }

        enableEdgeToEdge()

        setContent {
            GamebaseSampleProjectTheme {
                Box(
                    modifier = Modifier
                        .background(color = MaterialTheme.colors.primary)
                        .safeDrawingPadding()
                ) {
                    LoadStartScreen(
                        activity = this@MainActivity,
                        startRoute = startRoute,
                        isProcessRestart = isProcessRestarted(savedInstanceState),
                        shouldShowAccessInformationScreen = shouldShowAccessInformationScreen,
                    ) {
                        shouldShowAccessInformationScreen = false
                        putIntInPreference(
                            applicationContext,
                            KEY_LAST_ACCESS_INFO_SHOWN_VERSION,
                            BuildConfig.VERSION_CODE
                        )
                    }
                }
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putLong(APPLICATION_LAUNCHED_TIME, (application as GamebaseApplication).launchedTime)
    }

    private fun isProcessRestarted(savedInstanceState: Bundle?): Boolean {
        val savedApplicationLaunchedTime = savedInstanceState?.getLong(APPLICATION_LAUNCHED_TIME)
        return (application as GamebaseApplication).launchedTime != savedApplicationLaunchedTime
    }
}

@Composable
fun LoadStartScreen(
    activity: GamebaseActivity,
    startRoute: String,
    shouldShowAccessInformationScreen: Boolean,
    isProcessRestart: Boolean,
    updateVersionInPreferenceAndState: () -> Unit
) {
    if (shouldShowAccessInformationScreen) {
        AccessInformationScreen(updateVersionInPreferenceAndState)
    } else {
        MainScreen(
            activity = activity,
            startRoute = startRoute,
            isProcessRestart = isProcessRestart
        )
    }
}
