package com.jsoft.f_test.feature.face.uistate

sealed interface FaceUiState {
    data object CheckingPermission : FaceUiState

    data object PermissionDenied : FaceUiState

    data class Scanning(
        val consecutiveDetections: Int = 0,
        val requiredDetections: Int,
    ) : FaceUiState {
        val progress: Float get() = consecutiveDetections.toFloat() / requiredDetections
    }

    data object FaceDetected : FaceUiState
}