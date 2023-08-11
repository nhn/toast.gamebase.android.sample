package com.toast.android.gamebase.sample.ui

import android.content.Context
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Snackbar
import androidx.compose.material.SnackbarHost
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.toast.android.gamebase.base.NetworkManager
import com.toast.android.gamebase.sample.GamebaseActivity
import com.toast.android.gamebase.sample.R
import com.toast.android.gamebase.sample.gamebase_manager.mOnNetworkChangedListener
import com.toast.android.gamebase.sample.ui.navigation.SampleAppNavHost
import com.toast.android.gamebase.sample.ui.navigation.SampleAppScreens
import com.toast.android.gamebase.sample.ui.theme.Green
import com.toast.android.gamebase.sample.ui.theme.Grey700
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun MainScreen(
    activity: GamebaseActivity,
    startRoute: String
) {
    val navController = rememberNavController()
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    val networkState = remember { mutableStateOf(-1) }

    val currentBackStackEntry = navController.currentBackStackEntryAsState()
    val currentScreen = SampleAppScreens.fromRoute(
        currentBackStackEntry.value?.destination?.route
    )
    LaunchedEffect (Unit) {
        fun getNetworkStateMessage(
            context: Context,
            code: Int
        ) = when (code) {
            NetworkManager.TYPE_NOT -> context.resources.getString(R.string.network_changed_not)
            NetworkManager.TYPE_WIFI -> context.resources.getString(R.string.network_changed_wifi)
            NetworkManager.TYPE_MOBILE -> context.resources.getString(R.string.network_changed_mobile)
            NetworkManager.TYPE_ANY -> context.resources.getString(R.string.network_changed_any)
            else -> ""
        }
        mOnNetworkChangedListener = {
            networkState.value = it
            val networkStateMessage = getNetworkStateMessage(activity, it)
            if (networkStateMessage.isNotEmpty()) {
                scope.launch {
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = networkStateMessage,
                    )
                }
            }
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.background,
    ) {
        if (currentScreen.route == SampleAppScreens.Login.route || currentScreen.route == SampleAppScreens.Splash.route) {
            SampleAppNavHost(
                activity = activity,
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
                snackbarHost = {
                    SnackbarHost(
                        hostState = scaffoldState.snackbarHostState
                    ) {
                        Snackbar(
                            backgroundColor = getNetworkStateSnackbarColor(networkState.value),
                            modifier = Modifier
                                .padding(dimensionResource(id = R.dimen.common_snackbar_padding))
                        ) {
                            Text(
                                text = it.message,
                                color = Color.White
                            )
                        }
                    }
                },
                drawerContent = {
                    MainDrawer { route ->
                        onDestinationClicked(navController, scope, scaffoldState, route)
                    }
                }
            ) { innerPadding ->
                BackHandler(scaffoldState.drawerState.isOpen) {
                    scope.launch {
                        scaffoldState.drawerState.close()
                    }
                }
                SampleAppNavHost(
                    activity = activity,
                    navController = navController,
                    modifier = Modifier.padding(innerPadding),
                    startRoute = startRoute
                )
            }
        }
    }
}

private fun getNetworkStateSnackbarColor(
    code: Int
): Color =
    if (code == NetworkManager.TYPE_NOT) {
        // Network disconnected
        Grey700
    } else {
        // Network connected
        Green
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
