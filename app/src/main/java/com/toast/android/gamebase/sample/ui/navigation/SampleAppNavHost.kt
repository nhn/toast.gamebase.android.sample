package com.toast.android.gamebase.sample.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.toast.android.gamebase.sample.GamebaseActivity
import com.toast.android.gamebase.sample.ui.HomeScreen
import com.toast.android.gamebase.sample.ui.profile.ProfileScreen
import com.toast.android.gamebase.sample.ui.ShoppingScreen
import com.toast.android.gamebase.sample.ui.developer.DeveloperScreen
import com.toast.android.gamebase.sample.ui.developer.push.DeveloperPushSettingScreen
import com.toast.android.gamebase.sample.ui.idpmap.IdpMappingScreen
import com.toast.android.gamebase.sample.ui.login.LoginScreen
import com.toast.android.gamebase.sample.ui.settings.SettingsScreen
import com.toast.android.gamebase.sample.ui.gamebaseui.UIScreen
import kotlinx.coroutines.launch

@Composable
fun SampleAppNavHost(
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
            LoginScreen(
                navigateToHome = {
                    navController.navigate(SampleAppScreens.Home.route) {
                        popUpTo(SampleAppScreens.Login.route) {
                            inclusive = true
                        }
                    }
                }
            )
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
            SettingsScreen(
                navigateToLogin = {
                    navController.navigate(SampleAppScreens.Login.route) {
                        popUpTo(SampleAppScreens.Home.route) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                },
                navigateToIdpMappingScreen = {
                    navController.navigate(SampleAppScreens.IdpMapping.route) {
                        launchSingleTop = true
                    }
                }
            )
        }
        composable(SampleAppScreens.IdpMapping.route) {
            IdpMappingScreen()
        }
        composable(SampleAppScreens.UI.route) {
            UIScreen()
        }
        developerGraph(navController)
    }
}