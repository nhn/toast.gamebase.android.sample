package com.nhn.android.gamebase.sample

import androidx.annotation.StringRes

sealed class SampleAppScreen(val route: String, @StringRes val resourceId: Int) {
    object Home: SampleAppScreen("Home", R.string.home)
    object Shopping: SampleAppScreen("Shopping", R.string.shopping)
    object Profile: SampleAppScreen("Profile", R.string.profile)

    companion object {
        fun fromRoute(route: String?): SampleAppScreen =
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
    SampleAppScreen.Home,
    SampleAppScreen.Shopping,
    SampleAppScreen.Profile
    /*TODO: Add Screens*/
)