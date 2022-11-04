package com.toast.android.gamebase.sample.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.toast.android.gamebase.sample.ui.developer.DeveloperScreen
import com.toast.android.gamebase.sample.ui.developer.push.DeveloperPushSettingScreen
import com.toast.android.gamebase.sample.ui.developer.terms.DeveloperTermsSettingScreen

fun NavGraphBuilder.developerGraph(navController: NavController) {
    navigation(
        startDestination = SampleAppScreens.DeveloperRoot.route,
        route = SampleAppScreens.Developer.route) {

        composable(SampleAppScreens.DeveloperRoot.route) {
            DeveloperScreen(navController = navController)
        }
        composable(SampleAppScreens.DeveloperPushSetting.route) {
            DeveloperPushSettingScreen()
        }
        composable(SampleAppScreens.DeveloperTermsSetting.route) {
            DeveloperTermsSettingScreen()
        }
    }
}