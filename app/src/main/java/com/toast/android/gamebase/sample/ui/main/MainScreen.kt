package com.toast.android.gamebase.sample.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.toast.android.gamebase.sample.GamebaseActivity
import com.toast.android.gamebase.sample.R
import com.toast.android.gamebase.sample.ui.navigation.SampleAppNavHost
import com.toast.android.gamebase.sample.ui.navigation.SampleAppScreens
import com.toast.android.gamebase.sample.ui.navigation.screens
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun MainScreen(
    startRoute: String
) {
    val navController = rememberNavController()
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()

    val currentBackStackEntry = navController.currentBackStackEntryAsState()
    val currentScreen = SampleAppScreens.fromRoute(
        currentBackStackEntry.value?.destination?.route
    )
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.background,
    ) {
        if (currentScreen.route == SampleAppScreens.Login.route) {
            SampleAppNavHost(
                navController = navController,
                startRoute = startRoute
            )
        } else {
            Scaffold(
                topBar = {
                    AppBar(currentScreen) {
                        scope.launch {
                            scaffoldState.drawerState.open()
                        }
                    }
                },
                scaffoldState = scaffoldState,
                drawerContent = {
                    MainDrawer { route ->
                        onDestinationClicked(navController, scope, scaffoldState, route)
                    }
                }
            ) { innerPadding ->
                SampleAppNavHost(
                    navController = navController,
                    modifier = Modifier.padding(innerPadding),
                    startRoute = startRoute
                )
            }
        }
    }
}

private fun onDestinationClicked(
    navController: NavHostController,
    scope: CoroutineScope,
    scaffoldState: ScaffoldState,
    route: String
) {
    scope.launch {
        scaffoldState.drawerState.close()
    }
    try {
        navController.navigate(route) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    } catch (exception: IllegalArgumentException) {
        navController.navigate(SampleAppScreens.Home.route)
    }
}

@Composable
fun AppBar(
    currentScreen: SampleAppScreens,
    openDrawer: () -> Unit
) {
    TopAppBar(
        title = { Text(stringResource(currentScreen.resourceId)) },
        backgroundColor = MaterialTheme.colors.primary,
        navigationIcon = {
            IconButton(onClick = { openDrawer() }) {
                Icon(Icons.Filled.Menu, "list")
            }
        },
    )
}

@Composable
fun MainDrawer(
    onDestinationClicked: (route: String) -> Unit
) {
    Column(
        Modifier
            .wrapContentWidth()
            .padding(
                horizontal = dimensionResource(id = R.dimen.main_screen_drawer_padding_horizontal),
                vertical = dimensionResource(id = R.dimen.main_screen_drawer_padding_vertical)
            )
    ) {
        screens.forEach { screen ->
            Column (
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onDestinationClicked(screen.route)
                    }
                    .padding(
                        dimensionResource(id = R.dimen.main_screen_screen_menu_padding)
                    )
            ){
                Text(
                    text = stringResource(screen.resourceId),
                    style = MaterialTheme.typography.body1,
                )
            }
        }
    }
}

@Preview
@Composable
fun DrawablePreview() {
    MainDrawer {}
}
