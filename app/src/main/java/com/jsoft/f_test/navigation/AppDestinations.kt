package com.jsoft.f_test.navigation

import kotlinx.serialization.Serializable

sealed interface AppDestinations {
    @Serializable
    data object Login : AppDestinations

    @Serializable
    data object Face : AppDestinations

    @Serializable
    data object Home : AppDestinations
}

sealed interface HomeTabDestinations {
    @Serializable
    data object Movies : HomeTabDestinations

    @Serializable
    data object Map : HomeTabDestinations

    @Serializable
    data object Prayer : HomeTabDestinations

    @Serializable
    data object Weather : HomeTabDestinations
}