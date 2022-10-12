package com.nhn.android.gamebase.sample.ui

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.nhn.android.gamebase.sample.BuildConfig
import com.nhn.android.gamebase.sample.GamebaseActivity
import com.nhn.android.gamebase.sample.ui.navigation.SampleAppNavHost
import com.nhn.android.gamebase.sample.ui.navigation.SampleAppScreens
import com.nhn.android.gamebase.sample.ui.navigation.screens
import com.nhn.android.gamebase.sample.ui.theme.AccessInformationScreen
import com.nhn.android.gamebase.sample.ui.theme.GamebaseSampleProjectTheme
import com.nhn.android.gamebase.sample.util.getIntInPreference
import com.nhn.android.gamebase.sample.util.putIntInPreference
import kotlinx.coroutines.launch

class MainActivity : GamebaseActivity() {
    companion object {
        const val INTENT_APPLICATION_RELAUNCHED = "intent_key_is_application_relaunched"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val isApplicationRelaunched = intent.getBooleanExtra(INTENT_APPLICATION_RELAUNCHED, false)
        val startRoute = if (isApplicationRelaunched) SampleAppScreens.Home.route else SampleAppScreens.Login.route
        setContent {
            GamebaseSampleProjectTheme {
                LoadStartScreen(applicationContext = applicationContext, activity = this, startRoute = startRoute)
            }
        }
    }
}

@Composable
fun LoadStartScreen(applicationContext: Context, activity: GamebaseActivity, startRoute: String) {
    val versionCheck = "version"
    var goLoginDirect by remember { mutableStateOf(false) }

    if (getIntInPreference(applicationContext, versionCheck) == BuildConfig.VERSION_CODE) {
        goLoginDirect = true
    }

    if (goLoginDirect) {
        MainScreen(
            activity = activity,
            startRoute = startRoute
        )
    } else {
        AccessInformationScreen {
            goLoginDirect = true
        }
        putIntInPreference(applicationContext, versionCheck, BuildConfig.VERSION_CODE)
    }
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MainScreen(
    activity: GamebaseActivity,
    startRoute: String
) {
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
                if (currentScreen.route != "login" && currentScreen.route != "access_information") {
                    AppBar(currentScreen) {
                        scope.launch {
                            scaffoldState.drawerState.open()
                        }
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
                activity = activity,
                navController = navController,
                modifier = Modifier.padding(innerPadding),
                startRoute = startRoute
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
