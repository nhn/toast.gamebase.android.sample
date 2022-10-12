package com.nhn.android.gamebase.sample.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.nhn.android.gamebase.sample.GamebaseActivity
import com.nhn.android.gamebase.sample.ui.HomeScreen
import com.nhn.android.gamebase.sample.ui.ProfileScreen
import com.nhn.android.gamebase.sample.ui.ShoppingScreen
import com.nhn.android.gamebase.sample.ui.login.LoginScreen
import com.nhn.android.gamebase.sample.ui.settings.SettingsScreen


@Composable
fun SampleAppNavHost(
    activity: GamebaseActivity,
    navController: NavHostController,
    modifier: Modifier = Modifier,
    startRoute: String = SampleAppScreens.Login.route,
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
            HomeScreen()
        }
        composable(SampleAppScreens.Shopping.route) {
            ShoppingScreen()
        }
        composable(SampleAppScreens.Profile.route) {
            ProfileScreen()
        }
        composable(SampleAppScreens.Settings.route) {
            SettingsScreen(activity, navController = navController)
        }
    }
}