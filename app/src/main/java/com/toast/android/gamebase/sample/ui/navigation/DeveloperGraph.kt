package com.toast.android.gamebase.sample.ui.navigation

import android.app.Activity
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.toast.android.gamebase.sample.ui.developer.DeveloperScreen
import com.toast.android.gamebase.sample.ui.developer.push.DeveloperPushSettingScreen

fun NavGraphBuilder.developerGraph(navController: NavController, activity: Activity) {
    navigation(
        startDestination = SampleAppScreens.DeveloperRoot.route,
        route = SampleAppScreens.Developer.route) {

        composable(SampleAppScreens.DeveloperRoot.route) {
            DeveloperScreen(activity, navController = navController)
        }
        composable(SampleAppScreens.DeveloperPushSetting.route) {
            DeveloperPushSettingScreen()
        }
    }
}