package com.ak.feastit.compose.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.ak.feastit.compose.R
import com.ak.feastit.compose.ui.features.onboarding.OnboardingScreen
import com.ak.feastit.compose.ui.features.splash.SplashScreen
import com.ak.feastit.compose.ui.navigation.Screen

@Composable
internal fun FeastItApp(
    windowSizeClass: WindowSizeClass,
    navController: NavHostController = rememberNavController()
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val showNavigation = currentDestination?.route in listOf(
        Screen.Explore.route,
        Screen.Collection.route,
        Screen.Settings.route
    )

    val useNavRail = windowSizeClass.widthSizeClass > WindowWidthSizeClass.Compact

    Scaffold(
        bottomBar = {
            if (showNavigation && !useNavRail) {
                FeastBottomBar(navController, currentDestination)
            }
        }
    ) { innerPadding ->
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (showNavigation && useNavRail) {
                FeastNavRail(navController, currentDestination)
            }

            Box(modifier = Modifier.fillMaxSize()) {
                NavHost(
                    navController = navController,
                    startDestination = Screen.Splash.route
                ) {
                    composable(Screen.Splash.route) {
                        SplashScreen(
                            onNavigateToOnboarding = {
                                navController.navigate(Screen.Onboarding.route) {
                                    popUpTo(Screen.Splash.route) { inclusive = true }
                                }
                            },
                            onNavigateToHome = {
                                navController.navigate(Screen.Explore.route) {
                                    popUpTo(Screen.Splash.route) { inclusive = true }
                                }
                            }
                        )
                    }
                    composable(Screen.Onboarding.route) {
                        OnboardingScreen(
                            onNavigateToHome = {
                                navController.navigate(Screen.Explore.route) {
                                    popUpTo(Screen.Onboarding.route) { inclusive = true }
                                }
                            }
                        )
                    }
                    composable(Screen.Explore.route) {
                        Text("Explore Screen")
                    }
                    composable(Screen.Collection.route) {
                        Text("Collection Screen")
                    }
                    composable(Screen.Settings.route) {
                        Text("Settings Screen")
                    }
                }
            }
        }
    }
}

@Composable
private fun FeastBottomBar(
    navController: NavHostController,
    currentDestination: androidx.navigation.NavDestination?
) {
    NavigationBar {
        bottomNavItems.forEach { item ->
            val selected = currentDestination?.hierarchy?.any { it.route == item.screen.route } == true
            NavigationBarItem(
                icon = { Icon(painterResource(id = item.icon), contentDescription = null) },
                label = { Text(stringResource(id = item.title)) },
                selected = selected,
                onClick = {
                    navController.navigate(item.screen.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}

@Composable
private fun FeastNavRail(
    navController: NavHostController,
    currentDestination: androidx.navigation.NavDestination?
) {
    NavigationRail {
        bottomNavItems.forEach { item ->
            val selected = currentDestination?.hierarchy?.any { it.route == item.screen.route } == true
            NavigationRailItem(
                icon = { Icon(painterResource(id = item.icon), contentDescription = null) },
                label = { Text(stringResource(id = item.title)) },
                selected = selected,
                onClick = {
                    navController.navigate(item.screen.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}

private data class NavigationItem(
    val screen: Screen,
    val title: Int,
    val icon: Int
)

private val bottomNavItems = listOf(
    NavigationItem(Screen.Explore, R.string.home, R.drawable.icon_explore),
    NavigationItem(Screen.Collection, R.string.menu_collections, R.drawable.icon_collection),
    NavigationItem(Screen.Settings, R.string.menu_settings, R.drawable.icon_settings)
)
