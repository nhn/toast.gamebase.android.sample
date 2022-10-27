package com.toast.android.gamebase.sample.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.toast.android.gamebase.sample.GamebaseActivity
import com.toast.android.gamebase.sample.ui.HomeScreen
import com.toast.android.gamebase.sample.ui.profile.ProfileScreen
import com.toast.android.gamebase.sample.ui.ShoppingScreen
import com.toast.android.gamebase.sample.ui.developer.DeveloperScreen
import com.toast.android.gamebase.sample.ui.login.LoginScreen
import com.toast.android.gamebase.sample.ui.settings.SettingsScreen
import com.toast.android.gamebase.sample.ui.gamebaseui.UIScreen

@Composable
fun SampleAppNavHost(
    activity: GamebaseActivity,
    navController: NavHostController,
    modifier: Modifier = Modifier,
    startRoute: String = SampleAppScreens.Login.route
) {
    NavHost(
        navController = navController,
        startDestination = startRoute,
        modifier = modifier
    ) {
        composable(SampleAppScreens.Login.route) {
            LoginScreen(activity, navController = navController)
        }
        composable(SampleAppScreens.Home.route) {
            HomeScreen(activity)
        }
        composable(SampleAppScreens.Shopping.route) {
            ShoppingScreen(activity)
        }
        composable(SampleAppScreens.Profile.route) {
            ProfileScreen()
        }
        composable(SampleAppScreens.Settings.route) {
            SettingsScreen(activity, navController = navController)
        }
        composable(SampleAppScreens.UI.route) {
            UIScreen(activity = activity)
        }
        composable(SampleAppScreens.Developer.route) {
            DeveloperScreen()
        }
    }
}