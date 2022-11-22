package com.toast.android.gamebase.sample.ui

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.toast.android.gamebase.Gamebase
import com.toast.android.gamebase.sample.BuildConfig
import com.toast.android.gamebase.sample.GamebaseActivity
import com.toast.android.gamebase.sample.ui.access_info.AccessInformationScreen
import com.toast.android.gamebase.sample.ui.main.MainScreen
import com.toast.android.gamebase.sample.ui.navigation.SampleAppScreens
import com.toast.android.gamebase.sample.ui.theme.GamebaseSampleProjectTheme
import com.toast.android.gamebase.sample.util.getIntInPreference
import com.toast.android.gamebase.sample.util.putIntInPreference

class MainActivity : GamebaseActivity() {
    companion object {
        const val KEY_LAST_ACCESS_INFO_SHOWN_VERSION = "gamebase.sample.pref.access.info.shown.version"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val startRoute =
            if (Gamebase.isInitialized()) SampleAppScreens.Home.route else SampleAppScreens.Splash.route
        var shouldShowAccessInformationScreen by mutableStateOf(true)

        if (getIntInPreference(applicationContext, KEY_LAST_ACCESS_INFO_SHOWN_VERSION, -1) == BuildConfig.VERSION_CODE) {
            shouldShowAccessInformationScreen = false
        }
        setContent {
            GamebaseSampleProjectTheme {
                LoadStartScreen(
                    activity = this,
                    startRoute = startRoute,
                    shouldShowAccessInformationScreen = shouldShowAccessInformationScreen
                ) {
                    shouldShowAccessInformationScreen = false
                    putIntInPreference(applicationContext, KEY_LAST_ACCESS_INFO_SHOWN_VERSION, BuildConfig.VERSION_CODE)
                }
            }
        }
    }
}

@Composable
fun LoadStartScreen(
    activity: GamebaseActivity,
    startRoute: String,
    shouldShowAccessInformationScreen: Boolean,
    updateVersionInPreferenceAndState: () -> Unit
) {
    if (shouldShowAccessInformationScreen) {
        AccessInformationScreen(updateVersionInPreferenceAndState)
    } else {
        MainScreen(
            activity = activity,
            startRoute = startRoute
        )
    }
}
