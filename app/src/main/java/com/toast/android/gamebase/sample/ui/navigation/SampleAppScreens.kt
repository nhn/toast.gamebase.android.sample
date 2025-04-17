package com.toast.android.gamebase.sample.ui.navigation

import androidx.annotation.StringRes
import com.toast.android.gamebase.sample.R

sealed class SampleAppScreens(val route: String, @StringRes val resourceId: Int) {
    object Splash : SampleAppScreens("splash", R.string.splash)
    object Login: SampleAppScreens("login", R.string.login)
    object Home: SampleAppScreens("home", R.string.home)
    object Shopping: SampleAppScreens("shopping", R.string.shopping)
    object Profile: SampleAppScreens("profile", R.string.profile)
    object Settings: SampleAppScreens("settings", R.string.settings)
    object IdpMapping: SampleAppScreens("idp_mapping", R.string.idp_mapping)
    object Developer: SampleAppScreens("developer", R.string.developer)

    object DeveloperRoot: SampleAppScreens("dev_home", R.string.developer)
    object DeveloperPushSetting: SampleAppScreens("push_setting", R.string.developer_menu_category_push)
    object DeveloperTermsSetting: SampleAppScreens("terms_setting", R.string.developer_menu_category_terms)
    object DeveloperCustomTermsSetting: SampleAppScreens("terms_custom_setting", R.string.developer_menu_category_terms)
    object DeveloperContactDetail: SampleAppScreens("contact_detail", R.string.developer_menu_category_contact)
    object DeveloperCustomImageNoticeSetting: SampleAppScreens("image_notice_setting", R.string.developer_menu_category_image_notice)
    object DeveloperCustomGameNoticeSetting: SampleAppScreens("game_notice_setting", R.string.developer_menu_category_game_notice)
    object DeveloperCustomWebViewSetting: SampleAppScreens("web_view_setting", R.string.developer_menu_category_webview)

    companion object {
        fun fromRoute(route: String?): SampleAppScreens =
            when (route?.substringBefore("/")) {
                Splash.route -> Splash
                Login.route -> Login
                Home.route -> Home
                Shopping.route -> Shopping
                Profile.route -> Profile
                Settings.route -> Settings
                IdpMapping.route -> IdpMapping
                Developer.route -> Developer
                DeveloperRoot.route -> DeveloperRoot
                DeveloperPushSetting.route -> DeveloperPushSetting
                DeveloperTermsSetting.route -> DeveloperTermsSetting
                DeveloperCustomTermsSetting.route -> DeveloperCustomTermsSetting
                DeveloperContactDetail.route -> DeveloperContactDetail
                DeveloperCustomImageNoticeSetting.route -> DeveloperCustomImageNoticeSetting
                DeveloperCustomGameNoticeSetting.route -> DeveloperCustomGameNoticeSetting
                DeveloperCustomWebViewSetting.route -> DeveloperCustomWebViewSetting
                null -> Home
                else -> throw IllegalArgumentException("Route $route is not recognized.")
            }
    }
}

val screens = listOf(
    SampleAppScreens.Home,
    SampleAppScreens.Shopping,
    SampleAppScreens.Profile,
    SampleAppScreens.Settings,
    SampleAppScreens.Developer
)
