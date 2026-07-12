package com.jsoft.f_test.feature.face.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jsoft.f_test.domain.auth.repository.FaceDetectionEngine
import com.jsoft.f_test.feature.face.uistate.FaceUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FaceViewModel(
    val faceDetectionEngine: FaceDetectionEngine,
) : ViewModel() {

    private val _uiState = MutableStateFlow<FaceUiState>(FaceUiState.CheckingPermission)
    val uiState: StateFlow<FaceUiState> = _uiState.asStateFlow()

    init {
        observeDetections()
    }

    private fun observeDetections() {
        viewModelScope.launch {
            faceDetectionEngine.results.collect { result ->
                val currentState = _uiState.value
                if (currentState !is FaceUiState.Scanning) return@collect

                if (result.isValid) {
                    val newCount = currentState.consecutiveDetections + 1
                    if (newCount >= currentState.requiredDetections) {
                        _uiState.value = FaceUiState.FaceDetected
                    } else {
                        _uiState.value = currentState.copy(consecutiveDetections = newCount)
                    }
                } else {
                    if (currentState.consecutiveDetections > 0) {
                        _uiState.value = currentState.copy(consecutiveDetections = 0)
                    }
                }
            }
        }
    }

    fun onPermissionGranted() {
        _uiState.value = FaceUiState.Scanning(
            consecutiveDetections = 0,
            requiredDetections = REQUIRED_DETECTIONS,
        )
    }

    fun onPermissionDenied() {
        _uiState.value = FaceUiState.PermissionDenied
    }

    override fun onCleared() {
        faceDetectionEngine.release()
    }

    companion object {
        private const val REQUIRED_DETECTIONS = 30
    }
}