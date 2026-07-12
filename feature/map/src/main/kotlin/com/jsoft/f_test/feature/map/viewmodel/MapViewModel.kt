package com.jsoft.f_test.feature.map.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.jsoft.f_test.core.common.result.AppError
import com.jsoft.f_test.core.common.result.AppResult
import com.jsoft.f_test.domain.location.usecase.GetCurrentLocationUseCase
import com.jsoft.f_test.feature.map.uistate.MapUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MapViewModel(
    private val getCurrentLocation: GetCurrentLocationUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(MapUiState())
    val uiState: StateFlow<MapUiState> = _uiState.asStateFlow()

    fun fetchUserLocation() {
        viewModelScope.launch {
            _uiState.update { 
                it.copy(isRefreshing = true, errorMessage = null, permissionRequired = false) 
            }

            when (val result = getCurrentLocation()) {
                is AppResult.Success -> {
                    _uiState.update {
                        it.copy(
                            isRefreshing = false,
                            userLocation = LatLng(result.data.latitude, result.data.longitude)
                        )
                    }
                }

                is AppResult.Error -> {
                    handleLocationError(result.error)
                }
            }
        }
    }

    fun onPermissionGranted() {
        fetchUserLocation()
    }

    fun dismissError() {
        _uiState.update { it.copy(errorMessage = null) }
    }

    private fun handleLocationError(error: AppError) {
        when (error) {
            is AppError.PermissionDenied -> {
                _uiState.update {
                    it.copy(isRefreshing = false, permissionRequired = true)
                }
            }

            is AppError.LocationDisabled -> {
                _uiState.update {
                    it.copy(
                        isRefreshing = false,
                        errorMessage = "Joylashuv xizmati o'chirilgan",
                    )
                }
            }

            else -> {
                _uiState.update {
                    it.copy(
                        isRefreshing = false,
                        errorMessage = error.message ?: "Joyni aniqlab bo'lmadi",
                    )
                }
            }
        }
    }
}
