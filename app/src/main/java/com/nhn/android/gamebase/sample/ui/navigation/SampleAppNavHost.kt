package com.nhn.android.gamebase.sample.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.nhn.android.gamebase.sample.ui.HomeScreen
import com.nhn.android.gamebase.sample.ui.ProfileScreen
import com.nhn.android.gamebase.sample.ui.ShoppingScreen
import com.nhn.android.gamebase.sample.ui.login.LoginScreen


@Composable
fun SampleAppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = SampleAppScreens.Login.route,
        modifier = modifier
    ) {
        composable(SampleAppScreens.Login.route) {
            LoginScreen() {
                navController.navigate(SampleAppScreens.Home.route) {
                    popUpTo(SampleAppScreens.Login.route) {
                        inclusive = true
                    }
                }
            }
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
    }
}