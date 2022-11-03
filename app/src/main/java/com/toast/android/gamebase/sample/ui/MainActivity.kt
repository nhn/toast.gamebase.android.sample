package com.toast.android.gamebase.sample.ui

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.toast.android.gamebase.sample.BuildConfig
import com.toast.android.gamebase.sample.GamebaseActivity
import com.toast.android.gamebase.sample.gamebasemanager.addGamebaseEventHandler
import com.toast.android.gamebase.sample.ui.access.AccessInformationScreen
import com.toast.android.gamebase.sample.ui.main.MainScreen
import com.toast.android.gamebase.sample.ui.navigation.SampleAppScreens
import com.toast.android.gamebase.sample.ui.theme.GamebaseSampleProjectTheme
import com.toast.android.gamebase.sample.util.getIntInPreference
import com.toast.android.gamebase.sample.util.putIntInPreference

class MainActivity : GamebaseActivity() {
    companion object {
        const val INTENT_APPLICATION_RELAUNCHED = "intent_key_is_application_relaunched"
        const val KEY_LAST_ACCESS_INFO_SHOWN_VERSION = "gamebase.sample.pref.access.info.shown.version"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val isApplicationRelaunched = intent.getBooleanExtra(INTENT_APPLICATION_RELAUNCHED, false)
        val startRoute =
            if (isApplicationRelaunched) SampleAppScreens.Home.route else SampleAppScreens.Login.route
        var shouldShowAccessInformationScreen by mutableStateOf(true)

        if (getIntInPreference(applicationContext, KEY_LAST_ACCESS_INFO_SHOWN_VERSION, -1) == BuildConfig.VERSION_CODE) {
            shouldShowAccessInformationScreen = false
        }

        setContent {
            GamebaseSampleProjectTheme {
                LoadStartScreen(
                    startRoute = startRoute,
                    shouldShowAccessInformationScreen = shouldShowAccessInformationScreen
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
        // Handles All of Gamebase events.
        addGamebaseEventHandler(this)
    }
}

@Composable
fun LoadStartScreen(
    startRoute: String,
    shouldShowAccessInformationScreen: Boolean,
    updateVersionInPreferenceAndState: () -> Unit
) {
    if (shouldShowAccessInformationScreen) {
        AccessInformationScreen(updateVersionInPreferenceAndState)
    } else {
        MainScreen(
            startRoute = startRoute
        )
    }
}
