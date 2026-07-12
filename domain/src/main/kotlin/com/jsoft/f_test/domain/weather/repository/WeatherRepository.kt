package com.jsoft.f_test.domain.weather.repository

import com.jsoft.f_test.core.common.result.AppResult
import com.jsoft.f_test.domain.location.model.Coordinates
import com.jsoft.f_test.domain.weather.model.Weather
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {
    fun observeWeather(): Flow<Weather?>
    suspend fun refreshWeather(coordinates: Coordinates): AppResult<Unit>
}