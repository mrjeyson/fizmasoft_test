package com.jsoft.f_test.feature.weather.ui

import android.Manifest
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberPermissionState
import com.jsoft.f_test.feature.weather.viewmodel.WeatherViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun WeatherScreen(
    viewModel: WeatherViewModel = koinViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    val locationPermission = rememberPermissionState(
        permission = Manifest.permission.ACCESS_COARSE_LOCATION,
    )

    LaunchedEffect(locationPermission.status) {
        if (locationPermission.status is PermissionStatus.Granted) {
            viewModel.onPermissionGranted()
        }
    }

    WeatherContent(
        state = state,
        onRefresh = viewModel::refresh,
        onRequestPermission = { locationPermission.launchPermissionRequest() },
        onErrorDismissed = viewModel::dismissError,
    )
}