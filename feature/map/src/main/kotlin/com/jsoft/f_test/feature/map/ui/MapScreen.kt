package com.jsoft.f_test.feature.map.ui

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.IntentSender
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.rememberCameraPositionState
import com.jsoft.f_test.feature.map.viewmodel.MapViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MapScreen(
    viewModel: MapViewModel = koinViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val defaultLocation = LatLng(0.0, 0.0)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(defaultLocation, 2f)
    }

    val locationPermissionState = rememberPermissionState(
        Manifest.permission.ACCESS_FINE_LOCATION
    )

    val settingResultRequest = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult()
    ) { activityResult ->
        if (activityResult.resultCode == Activity.RESULT_OK) {
            viewModel.fetchUserLocation()
        }
    }

    LaunchedEffect(Unit) {
        if (!locationPermissionState.status.isGranted) {
            locationPermissionState.launchPermissionRequest()
        }
    }

    LaunchedEffect(locationPermissionState.status.isGranted) {
        if (locationPermissionState.status.isGranted) {
            checkLocationSettings(
                context = context,
                onLocationSettingsUnsatisfied = { intentSenderRequest ->
                    settingResultRequest.launch(intentSenderRequest)
                },
                onLocationSettingsSatisfied = {
                    viewModel.fetchUserLocation()
                }
            )
        }
    }

    MapContent(
        state = state,
        isPermissionGranted = locationPermissionState.status.isGranted,
        cameraPositionState = cameraPositionState,
        onLocationClick = {
            if (locationPermissionState.status.isGranted) {
                checkLocationSettings(
                    context = context,
                    onLocationSettingsUnsatisfied = { intentSenderRequest ->
                        settingResultRequest.launch(intentSenderRequest)
                    },
                    onLocationSettingsSatisfied = {
                        viewModel.fetchUserLocation()
                    }
                )
            } else {
                locationPermissionState.launchPermissionRequest()
            }
        },
        onZoomIn = {
            scope.launch {
                cameraPositionState.animate(CameraUpdateFactory.zoomIn())
            }
        },
        onZoomOut = {
            scope.launch {
                cameraPositionState.animate(CameraUpdateFactory.zoomOut())
            }
        }
    )
}

private fun checkLocationSettings(
    context: Context,
    onLocationSettingsUnsatisfied: (IntentSenderRequest) -> Unit,
    onLocationSettingsSatisfied: () -> Unit
) {
    val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 1000)
        .build()

    val builder = LocationSettingsRequest.Builder()
        .addLocationRequest(locationRequest)

    val client = LocationServices.getSettingsClient(context)
    val task = client.checkLocationSettings(builder.build())

    task.addOnSuccessListener {
        onLocationSettingsSatisfied()
    }

    task.addOnFailureListener { exception ->
        if (exception is ResolvableApiException) {
            try {
                val intentSenderRequest = IntentSenderRequest.Builder(exception.resolution).build()
                onLocationSettingsUnsatisfied(intentSenderRequest)
            } catch (sendEx: IntentSender.SendIntentException) {
                // Ignore the error.
            }
        }
    }
}
