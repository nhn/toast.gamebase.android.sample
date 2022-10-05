package com.nhn.android.gamebase.sample.ui

import androidx.annotation.StringRes
import com.nhn.android.gamebase.sample.R

sealed class SampleAppScreens(val route: String, @StringRes val resourceId: Int) {
    object Home: SampleAppScreens("home", R.string.home)
    object Shopping: SampleAppScreens("shopping", R.string.shopping)
    object Profile: SampleAppScreens("profile", R.string.profile)

    companion object {
        fun fromRoute(route: String?): SampleAppScreens =
            when (route?.substringBefore("/")) {
                Home.route -> Home
                Shopping.route -> Shopping
                Profile.route -> Profile
                null -> Home
                else -> throw IllegalArgumentException("Route $route is not recognized.")
            }
    }
}

val screens = listOf(
    SampleAppScreens.Home,
    SampleAppScreens.Shopping,
    SampleAppScreens.Profile
    /*TODO: Add Screens*/
)