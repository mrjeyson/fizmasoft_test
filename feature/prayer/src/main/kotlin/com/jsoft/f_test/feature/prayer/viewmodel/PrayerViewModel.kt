package com.jsoft.f_test.feature.prayer.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jsoft.f_test.core.common.result.AppError
import com.jsoft.f_test.core.common.result.AppResult
import com.jsoft.f_test.domain.location.usecase.GetCurrentLocationUseCase
import com.jsoft.f_test.domain.prayer.usecase.GetTodayPrayerTimesUseCase
import com.jsoft.f_test.domain.prayer.usecase.RefreshPrayerTimesUseCase
import com.jsoft.f_test.domain.prayer.usecase.SchedulePrayerAlarmsUseCase
import com.jsoft.f_test.feature.prayer.uistate.PrayerUiState
import com.jsoft.f_test.feature.prayer.uistate.formatDisplayDate
import com.jsoft.f_test.feature.prayer.uistate.nextPrayerIndex
import com.jsoft.f_test.feature.prayer.uistate.toUiList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PrayerViewModel(
    private val getTodayPrayerTimes: GetTodayPrayerTimesUseCase,
    private val refreshPrayerTimes: RefreshPrayerTimesUseCase,
    private val getCurrentLocation: GetCurrentLocationUseCase,
    private val schedulePrayerAlarms: SchedulePrayerAlarmsUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(PrayerUiState())
    val uiState: StateFlow<PrayerUiState> = _uiState.asStateFlow()

    init {
        getTodayPrayerTimes()
            .onEach { prayerTimes ->
                if (prayerTimes != null) {
                    _uiState.update {
                        it.copy(
                            prayers = prayerTimes.toUiList(),
                            dateText = formatDisplayDate(prayerTimes.date),
                            nextPrayerIndex = prayerTimes.nextPrayerIndex(),
                        )
                    }
                    schedulePrayerAlarms(prayerTimes)
                }
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
                    when (val refreshResult = refreshPrayerTimes(locationResult.data)) {
                        is AppResult.Success -> {
                            _uiState.update { it.copy(isRefreshing = false) }
                        }

                        is AppResult.Error -> {
                            _uiState.update {
                                it.copy(
                                    isRefreshing = false,
                                    errorMessage = refreshResult.error.message,
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