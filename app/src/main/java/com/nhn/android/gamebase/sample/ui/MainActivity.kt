package com.nhn.android.gamebase.sample.ui

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.nhn.android.gamebase.sample.GamebaseActivity
import com.nhn.android.gamebase.sample.ui.navigation.SampleAppNavHost
import com.nhn.android.gamebase.sample.ui.navigation.SampleAppScreens
import com.nhn.android.gamebase.sample.ui.navigation.screens
import com.nhn.android.gamebase.sample.ui.theme.GamebaseSampleProjectTheme
import kotlinx.coroutines.launch

class MainActivity : GamebaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GamebaseSampleProjectTheme {
                MainScreen()
            }
        }
    }
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MainScreen() {
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    val navController = rememberNavController()

    val currentBackStackEntry = navController.currentBackStackEntryAsState()
    val currentScreen = SampleAppScreens.fromRoute(
        currentBackStackEntry.value?.destination?.route
    )
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.background,
    ) {
        Scaffold (
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
            },
        ){ innerPadding ->
            SampleAppNavHost(
                navController = navController,
                modifier = Modifier.padding(innerPadding)
            )
        }
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
            .padding(start = 24.dp, top = 48.dp)
    ) {
        screens.forEach { screen ->
            Spacer(Modifier.height(24.dp))
            Text(
                text = stringResource(screen.resourceId),
                style = MaterialTheme.typography.body1,
                modifier = Modifier.clickable {
                    onDestinationClicked(screen.route)
                }
            )
        }
    }
}

@Preview
@Composable
fun DrawablePreview() {
    MainDrawer {}
}
