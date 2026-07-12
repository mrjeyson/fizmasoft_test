package com.jsoft.f_test.domain.weather.model

data class Weather(
    val temperatureCelsius: Double,
    val condition: WeatherCondition,
    val windSpeedKmh: Double,
    val updatedAt: Long,
)

enum class WeatherCondition {
    Clear,
    PartlyCloudy,
    Cloudy,
    Fog,
    Drizzle,
    Rain,
    Snow,
    Thunderstorm,
    Unknown,
}