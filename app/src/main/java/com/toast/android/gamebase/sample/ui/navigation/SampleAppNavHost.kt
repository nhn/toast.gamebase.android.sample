package com.toast.android.gamebase.sample.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.toast.android.gamebase.sample.GamebaseActivity
import com.toast.android.gamebase.sample.ui.HomeScreen
import com.toast.android.gamebase.sample.ui.ShoppingScreen
import com.toast.android.gamebase.sample.ui.idpmap.IdpMappingScreen
import com.toast.android.gamebase.sample.ui.login.LoginScreen
import com.toast.android.gamebase.sample.ui.profile.ProfileScreen
import com.toast.android.gamebase.sample.ui.settings.SettingsScreen
import com.toast.android.gamebase.sample.ui.splash.SplashScreen

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
        composable(SampleAppScreens.Splash.route) {
            SplashScreen(activity, onInitialized = {
                navController.navigate(SampleAppScreens.Login.route) {
                    popUpTo(SampleAppScreens.Splash.route) {
                        inclusive = true
                    }
                    launchSingleTop = true
                }
            })
        }
        composable(SampleAppScreens.Login.route) {
            LoginScreen(
                activity = activity,
                onLoggedIn = {
                    navController.navigate(SampleAppScreens.Home.route) {
                        popUpTo(SampleAppScreens.Login.route) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                }
            )
        }
        composable(SampleAppScreens.Home.route) {
            HomeScreen(
                activity = activity,
                onLoggedOut = {
                    onLoggedOut(navController)
                }
            )
        }
        composable(SampleAppScreens.Shopping.route) {
            ShoppingScreen(activity)
        }
        composable(SampleAppScreens.Profile.route) {
            ProfileScreen()
        }
        composable(SampleAppScreens.Settings.route) {
            SettingsScreen(
                activity = activity,
                onLoggedOut = {
                    onLoggedOut(navController)
                },
                navigateToIdpMapping = {
                    navController.navigate(SampleAppScreens.IdpMapping.route) {
                        launchSingleTop = true
                    }
                })
        }
        composable(SampleAppScreens.IdpMapping.route) {
            IdpMappingScreen(activity = activity)
        }
        developerGraph(navController, activity)
    }
}

private fun onLoggedOut(navController: NavHostController) {
    navController.navigate(SampleAppScreens.Login.route) {
        popUpTo(SampleAppScreens.Home.route) {
            inclusive = true
        }
        launchSingleTop = true
    }
}