package com.jsoft.f_test.feature.face.ui

import androidx.camera.core.ImageAnalysis
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jsoft.f_test.core.ui.theme.F_testTheme
import com.jsoft.f_test.feature.face.uistate.FaceUiState

@Composable
fun FaceContent(
    state: FaceUiState,
    analyzer: ImageAnalysis.Analyzer?,
    onRequestPermission: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.fillMaxSize()) {
        when (state) {
            is FaceUiState.CheckingPermission -> {
                CenteredLoading()
            }

            is FaceUiState.PermissionDenied -> {
                PermissionDeniedView(onRequestPermission = onRequestPermission)
            }

            is FaceUiState.Scanning -> {
                if (analyzer != null) {
                    ScanningView(state = state, analyzer = analyzer)
                }
            }

            is FaceUiState.FaceDetected -> {
                DetectedView()
            }
        }
    }
}

@Composable
private fun CenteredLoading() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

@Composable
private fun PermissionDeniedView(onRequestPermission: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(
            imageVector = Icons.Filled.Face,
            contentDescription = null,
            modifier = Modifier.height(64.dp),
        )
        Spacer(Modifier.height(16.dp))
        Text(
            text = "Kameraga ruxsat kerak",
            style = MaterialTheme.typography.headlineSmall,
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = "Yuz orqali kirishni yakunlash uchun kameraga ruxsat bering.",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(Modifier.height(24.dp))
        Button(onClick = onRequestPermission) {
            Text("Ruxsat berish")
        }
    }
}

@Composable
private fun ScanningView(
    state: FaceUiState.Scanning,
    analyzer: ImageAnalysis.Analyzer,
) {
    Box(modifier = Modifier.fillMaxSize()) {
        CameraPreview(
            analyzer = analyzer,
            modifier = Modifier.fillMaxSize(),
        )

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(Color.Black.copy(alpha = 0.6f))
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = if (state.consecutiveDetections > 0) {
                    "Yuz aniqlandi, ushlab turing..."
                } else {
                    "Yuzingizni kameraga qarating"
                },
                color = Color.White,
                style = MaterialTheme.typography.bodyLarge,
            )
            Spacer(Modifier.height(12.dp))

            val animatedProgress by animateFloatAsState(
                targetValue = state.progress,
                label = "faceProgress",
            )
            LinearProgressIndicator(
                progress = { animatedProgress },
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(CircleShape),
            )
        }
    }
}

@Composable
private fun DetectedView() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primaryContainer),
        contentAlignment = Alignment.Center,
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = Icons.Filled.Face,
                contentDescription = null,
                modifier = Modifier.height(80.dp),
                tint = MaterialTheme.colorScheme.primary,
            )
            Spacer(Modifier.height(16.dp))
            Text(
                text = "Yuz muvaffaqiyatli aniqlandi",
                style = MaterialTheme.typography.headlineSmall,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PermissionDeniedPreview() {
    F_testTheme {
        FaceContent(
            state = FaceUiState.PermissionDenied,
            analyzer = null,
            onRequestPermission = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CheckingPermissionPreview() {
    F_testTheme {
        FaceContent(
            state = FaceUiState.CheckingPermission,
            analyzer = null,
            onRequestPermission = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun DetectedPreview() {
    F_testTheme {
        FaceContent(
            state = FaceUiState.FaceDetected,
            analyzer = null,
            onRequestPermission = {},
        )
    }
}