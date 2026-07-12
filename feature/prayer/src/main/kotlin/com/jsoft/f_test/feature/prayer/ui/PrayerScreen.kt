package com.jsoft.f_test.feature.prayer.ui

import android.Manifest
import android.os.Build
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.jsoft.f_test.feature.prayer.viewmodel.PrayerViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PrayerScreen(
    viewModel: PrayerViewModel = koinViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    val requiredPermissions = buildList {
        add(Manifest.permission.ACCESS_COARSE_LOCATION)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            add(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    val permissionsState = rememberMultiplePermissionsState(permissions = requiredPermissions)

    val locationGranted = permissionsState.permissions
        .firstOrNull { it.permission == Manifest.permission.ACCESS_COARSE_LOCATION }
        ?.status is PermissionStatus.Granted

    LaunchedEffect(locationGranted) {
        if (locationGranted) {
            viewModel.onPermissionGranted()
        }
    }

    PrayerContent(
        state = state,
        onRefresh = viewModel::refresh,
        onRequestPermission = { permissionsState.launchMultiplePermissionRequest() },
        onErrorDismissed = viewModel::dismissError,
    )
}