package com.jsoft.f_test.navigation.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.jsoft.f_test.feature.movies.ui.MoviesScreen
import com.jsoft.f_test.feature.prayer.ui.PrayerScreen
import com.jsoft.f_test.feature.weather.ui.WeatherScreen
import com.jsoft.f_test.feature.map.ui.MapScreen
import com.jsoft.f_test.navigation.HomeTabDestinations

@Composable
fun HomeScreen() {
    val navController = rememberNavController()

    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            NavigationBar {
                HomeTab.entries.forEach { tab ->
                    val isSelected = backStackEntry?.destination?.hierarchy?.any {
                        it.hasRoute(tab.destination::class)
                    } == true
                    NavigationBarItem(
                        selected = isSelected,
                        onClick = {
                            navController.navigate(tab.destination) {
                                popUpTo(navController.graph.startDestinationId) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { Icon(tab.icon, contentDescription = tab.label) },
                        label = { Text(tab.label) },
                    )
                }
            }
        },
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = HomeTabDestinations.Movies,
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
        ) {
            composable<HomeTabDestinations.Movies> {
                MoviesScreen()
            }
            composable<HomeTabDestinations.Map> {
//                TabPlaceholder("Map tab")
                MapScreen()

            }
            composable<HomeTabDestinations.Prayer> {
                PrayerScreen()
            }
            composable<HomeTabDestinations.Weather> {
                WeatherScreen()
            }
        }
    }
}

@Composable
private fun TabPlaceholder(name: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Text(name, style = MaterialTheme.typography.headlineMedium)
    }
}