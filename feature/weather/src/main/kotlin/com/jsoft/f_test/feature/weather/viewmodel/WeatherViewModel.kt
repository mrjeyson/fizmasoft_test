package com.jsoft.f_test.feature.weather.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jsoft.f_test.core.common.result.AppError
import com.jsoft.f_test.core.common.result.AppResult
import com.jsoft.f_test.domain.location.usecase.GetCurrentLocationUseCase
import com.jsoft.f_test.domain.weather.usecase.GetWeatherUseCase
import com.jsoft.f_test.domain.weather.usecase.RefreshWeatherUseCase
import com.jsoft.f_test.feature.weather.uistate.WeatherUiState
import com.jsoft.f_test.feature.weather.uistate.toUi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class WeatherViewModel(
    private val getWeather: GetWeatherUseCase,
    private val refreshWeather: RefreshWeatherUseCase,
    private val getCurrentLocation: GetCurrentLocationUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(WeatherUiState())
    val uiState: StateFlow<WeatherUiState> = _uiState.asStateFlow()

    init {
        getWeather()
            .onEach { weather ->
                _uiState.update { it.copy(weather = weather?.toUi()) }
            }
            .launchIn(viewModelScope)

        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(isRefreshing = true, errorMessage = null, permissionRequired = false)
            }

            when (val locationResult = getCurrentLocation()) {
                is AppResult.Success -> {
                    when (val weatherResult = refreshWeather(locationResult.data)) {
                        is AppResult.Success -> {
                            _uiState.update { it.copy(isRefreshing = false) }
                        }

                        is AppResult.Error -> {
                            _uiState.update {
                                it.copy(
                                    isRefreshing = false,
                                    errorMessage = weatherResult.error.message,
                                )
                            }
                        }
                    }
                }

                is AppResult.Error -> {
                    handleLocationError(locationResult.error)
                }
            }
        }
    }

    fun onPermissionGranted() {
        refresh()
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
                        errorMessage = "Joylashuv xizmati o'chirilgan. Sozlamalardan yoqing.",
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