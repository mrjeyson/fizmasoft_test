package com.jsoft.f_test.domain.weather.usecase

import com.jsoft.f_test.domain.weather.model.Weather
import com.jsoft.f_test.domain.weather.repository.WeatherRepository
import kotlinx.coroutines.flow.Flow

class GetWeatherUseCase(
    private val repository: WeatherRepository,
) {
    operator fun invoke(): Flow<Weather?> = repository.observeWeather()
}