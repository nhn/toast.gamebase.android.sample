package com.toast.android.gamebase.sample.ui.navigation

import android.app.Activity
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.toast.android.gamebase.sample.ui.developer.DeveloperMenuNavigator
import com.toast.android.gamebase.sample.ui.developer.DeveloperScreen
import com.toast.android.gamebase.sample.ui.developer.contact.ContactDetailScreen
import com.toast.android.gamebase.sample.ui.developer.imagenotice.ImageNoticeSettingScreen
import com.toast.android.gamebase.sample.ui.developer.push.PushSettingScreen
import com.toast.android.gamebase.sample.ui.developer.terms.TermsCustomScreen
import com.toast.android.gamebase.sample.ui.developer.terms.TermsSettingScreen
import com.toast.android.gamebase.sample.ui.developer.webview.WebViewSettingScreen

fun NavGraphBuilder.developerGraph(navController: NavController, activity: Activity) {
    navigation(
        startDestination = SampleAppScreens.DeveloperRoot.route,
        route = SampleAppScreens.Developer.route
    ) {

        composable(SampleAppScreens.DeveloperRoot.route) {
            DeveloperScreen(activity, menuNavigator = object : DeveloperMenuNavigator {
                override fun onPushSettingMenu() {
                    navController.navigate(SampleAppScreens.DeveloperPushSetting.route)
                }
                override fun onTermsSettingMenu() {
                    navController.navigate(SampleAppScreens.DeveloperTermsSetting.route)
                }
                override fun onTermsCustomMenu() {
                    navController.navigate(SampleAppScreens.DeveloperCustomTermsSetting.route)
                }
                override fun onContactDetailMenu() {
                    navController.navigate(SampleAppScreens.DeveloperContactDetail.route)
                }
                override fun onImageNoticeSettingMenu() {
                    navController.navigate(SampleAppScreens.DeveloperCustomImageNoticeSetting.route)
                }
                override fun onWebViewSettingMenu() {
                    navController.navigate(SampleAppScreens.DeveloperCustomWebViewSetting.route)
                }
            })
        }
        composable(SampleAppScreens.DeveloperPushSetting.route) {
            PushSettingScreen()
        }
        composable(SampleAppScreens.DeveloperTermsSetting.route) {
            TermsSettingScreen()
        }
        composable(SampleAppScreens.DeveloperCustomTermsSetting.route) {
            TermsCustomScreen()
        }
        composable(SampleAppScreens.DeveloperContactDetail.route) {
            ContactDetailScreen()
        }
        composable(SampleAppScreens.DeveloperCustomImageNoticeSetting.route) {
            ImageNoticeSettingScreen()
        }
        composable(SampleAppScreens.DeveloperCustomWebViewSetting.route) {
            WebViewSettingScreen()
        }
    }
}