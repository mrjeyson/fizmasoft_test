package com.jsoft.f_test.domain.weather.usecase

import com.jsoft.f_test.core.common.result.AppResult
import com.jsoft.f_test.domain.location.model.Coordinates
import com.jsoft.f_test.domain.weather.repository.WeatherRepository

class RefreshWeatherUseCase(
    private val repository: WeatherRepository,
) {
    suspend operator fun invoke(coordinates: Coordinates): AppResult<Unit> =
        repository.refreshWeather(coordinates)
}