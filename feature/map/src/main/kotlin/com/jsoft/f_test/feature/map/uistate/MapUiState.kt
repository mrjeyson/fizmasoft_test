package com.jsoft.f_test.feature.map.uistate

import com.google.android.gms.maps.model.LatLng

data class MapUiState(
    val userLocation: LatLng? = null,
    val isRefreshing: Boolean = false,
    val errorMessage: String? = null,
    val permissionRequired: Boolean = false,
)
