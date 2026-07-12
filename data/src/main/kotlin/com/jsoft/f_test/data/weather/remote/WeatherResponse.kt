package com.jsoft.f_test.data.weather.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WeatherResponse(
    val latitude: Double,
    val longitude: Double,
    @SerialName("current_weather") val currentWeather: CurrentWeatherDto,
)

@Serializable
data class CurrentWeatherDto(
    val temperature: Double,
    @SerialName("windspeed") val windSpeed: Double,
    @SerialName("weathercode") val weatherCode: Int,
    val time: String,
)