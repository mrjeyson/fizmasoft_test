package com.jsoft.f_test.feature.face.ui

import android.Manifest
import androidx.camera.core.ImageAnalysis
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberPermissionState
import com.jsoft.f_test.feature.face.uistate.FaceUiState
import com.jsoft.f_test.feature.face.viewmodel.FaceViewModel
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel
import kotlin.time.Duration.Companion.milliseconds

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun FaceScreen(
    onFaceDetected: () -> Unit,
    viewModel: FaceViewModel = koinViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    val cameraPermission = rememberPermissionState(
        permission = Manifest.permission.CAMERA,
    )

    LaunchedEffect(cameraPermission.status) {
        when (val status = cameraPermission.status) {
            is PermissionStatus.Granted -> viewModel.onPermissionGranted()
            is PermissionStatus.Denied -> {
                if (!status.shouldShowRationale) {
                    cameraPermission.launchPermissionRequest()
                } else {
                    viewModel.onPermissionDenied()
                }
            }
        }
    }

    LaunchedEffect(state) {
        if (state is FaceUiState.FaceDetected) {
            delay(1000.milliseconds)
            onFaceDetected()
        }
    }

    FaceContent(
        state = state,
        analyzer = viewModel.faceDetectionEngine as? ImageAnalysis.Analyzer,
        onRequestPermission = { cameraPermission.launchPermissionRequest() },
    )
}