package com.toast.android.gamebase.sample.ui.navigation

import androidx.annotation.StringRes
import com.toast.android.gamebase.sample.R

sealed class SampleAppScreens(val route: String, @StringRes val resourceId: Int) {
    object Login: SampleAppScreens("login", R.string.login)
    object Home: SampleAppScreens("home", R.string.home)
    object Shopping: SampleAppScreens("shopping", R.string.shopping)
    object Profile: SampleAppScreens("profile", R.string.profile)
    object Settings: SampleAppScreens("settings", R.string.settings)
    object IdpMapping: SampleAppScreens("idp_mapping", R.string.idp_mapping)
    object UI: SampleAppScreens("ui", R.string.ui)
    object Developer: SampleAppScreens("developer", R.string.developer)
    object DeveloperRoot: SampleAppScreens("dev_home", R.string.developer)
    object DeveloperPushSetting: SampleAppScreens("push_setting", R.string.developer_menu_category_push)
    object DeveloperContactDetail: SampleAppScreens("contact_detail", R.string.developer_menu_category_contact)

    companion object {
        fun fromRoute(route: String?): SampleAppScreens =
            when (route?.substringBefore("/")) {
                Login.route -> Login
                Home.route -> Home
                Shopping.route -> Shopping
                Profile.route -> Profile
                Settings.route -> Settings
                IdpMapping.route -> IdpMapping
                UI.route -> UI
                Developer.route -> Developer
                null -> Home
                DeveloperRoot.route -> DeveloperRoot
                DeveloperPushSetting.route -> DeveloperPushSetting
                DeveloperContactDetail.route -> DeveloperContactDetail
                else -> throw IllegalArgumentException("Route $route is not recognized.")
            }
    }
}

val screens = listOf(
    SampleAppScreens.Home,
    SampleAppScreens.Shopping,
    SampleAppScreens.Profile,
    SampleAppScreens.UI,
    SampleAppScreens.Settings,
    SampleAppScreens.Developer
    /*TODO: Add Screens*/
)
