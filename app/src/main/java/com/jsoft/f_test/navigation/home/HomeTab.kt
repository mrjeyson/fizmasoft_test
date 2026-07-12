package com.jsoft.f_test.navigation.home

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudQueue
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Mosque
import androidx.compose.material.icons.filled.Movie
import androidx.compose.ui.graphics.vector.ImageVector
import com.jsoft.f_test.navigation.HomeTabDestinations

enum class HomeTab(
    val label: String,
    val icon: ImageVector,
    val destination: HomeTabDestinations,
) {
    Movies(
        label = "Kinolar",
        icon = Icons.Filled.Movie,
        destination = HomeTabDestinations.Movies,
    ),
    Map(
        label = "Xarita",
        icon = Icons.Filled.LocationOn,
        destination = HomeTabDestinations.Map,
    ),
    Prayer(
        label = "Namoz",
        icon = Icons.Filled.Mosque,
        destination = HomeTabDestinations.Prayer,
    ),
    Weather(
        label = "Ob-havo",
        icon = Icons.Filled.CloudQueue,
        destination = HomeTabDestinations.Weather,
    )
}