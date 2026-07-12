package com.jsoft.f_test.feature.map.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.MarkerComposable
import com.google.maps.android.compose.MarkerState
import androidx.compose.ui.tooling.preview.Preview
import com.jsoft.f_test.feature.map.R
import com.jsoft.f_test.feature.map.uistate.MapUiState
import com.google.android.gms.maps.model.CameraPosition
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun MapContent(
    state: MapUiState,
    isPermissionGranted: Boolean,
    cameraPositionState: CameraPositionState,
    onLocationClick: () -> Unit,
    onZoomIn: () -> Unit,
    onZoomOut: () -> Unit,
    modifier: Modifier = Modifier,
) {
    // Animate camera when user location changes
    LaunchedEffect(state.userLocation) {
        state.userLocation?.let {
            cameraPositionState.animate(
                CameraUpdateFactory.newLatLngZoom(it, 15f)
            )
        }
    }

    val uiSettings = remember {
        MapUiSettings(zoomControlsEnabled = false, myLocationButtonEnabled = isPermissionGranted)
    }
    val properties = remember(state.permissionRequired, isPermissionGranted) {
        MapProperties(
            mapType = MapType.TERRAIN,
            isMyLocationEnabled = isPermissionGranted
        )
    }

    Box(modifier = modifier.fillMaxSize()) {
        GoogleMap(
            uiSettings = uiSettings,
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            properties = properties
        ) {
            state.userLocation?.let { latLng ->
                val markerState = remember(latLng) { MarkerState(position = latLng) }
                MarkerComposable(
                    state = markerState,
                ) {
                    Image(
                        painter = painterResource(R.drawable.pin),
                        contentDescription = "User Location Pin",
                    )
                }
            }
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 25.dp, end = 15.dp)
        ) {
            IconButton(
                modifier = Modifier
                    .padding(7.dp)
                    .clip(CircleShape)
                    .background(Color.White),
                onClick = onLocationClick
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_location),
                    contentDescription = "Location icon"
                )
            }
            IconButton(
                modifier = Modifier
                    .padding(7.dp)
                    .clip(CircleShape)
                    .background(Color.White),
                onClick = onZoomIn
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.add),
                    contentDescription = "Zoom in"
                )
            }
            IconButton(
                modifier = Modifier
                    .padding(7.dp)
                    .clip(CircleShape)
                    .background(Color.White),
                onClick = onZoomOut
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.minus),
                    contentDescription = "Zoom out"
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MapContentPreview() {
    MapContent(
        state = MapUiState(
            userLocation = LatLng(41.311081, 69.240562) // Tashkent
        ),
        isPermissionGranted = true,
        cameraPositionState = rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(LatLng(41.311081, 69.240562), 10f)
        },
        onLocationClick = {},
        onZoomIn = {},
        onZoomOut = {}
    )
}
