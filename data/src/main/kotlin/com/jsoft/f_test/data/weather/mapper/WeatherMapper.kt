package com.jsoft.f_test.data.weather.mapper

import com.jsoft.f_test.core.database.weather.WeatherEntity
import com.jsoft.f_test.data.weather.remote.WeatherResponse
import com.jsoft.f_test.domain.weather.model.Weather
import com.jsoft.f_test.domain.weather.model.WeatherCondition

internal fun WeatherResponse.toEntity(): WeatherEntity = WeatherEntity(
    temperatureCelsius = currentWeather.temperature,
    weatherCode = currentWeather.weatherCode,
    windSpeedKmh = currentWeather.windSpeed,
    latitude = latitude,
    longitude = longitude,
    updatedAt = System.currentTimeMillis(),
)

internal fun WeatherEntity.toDomain(): Weather = Weather(
    temperatureCelsius = temperatureCelsius,
    condition = weatherCode.toWeatherCondition(),
    windSpeedKmh = windSpeedKmh,
    updatedAt = updatedAt,
)

private fun Int.toWeatherCondition(): WeatherCondition = when (this) {
    0 -> WeatherCondition.Clear
    1, 2 -> WeatherCondition.PartlyCloudy
    3 -> WeatherCondition.Cloudy
    45, 48 -> WeatherCondition.Fog
    in 51..57 -> WeatherCondition.Drizzle
    in 61..67, in 80..82 -> WeatherCondition.Rain
    in 71..77, 85, 86 -> WeatherCondition.Snow
    in 95..99 -> WeatherCondition.Thunderstorm
    else -> WeatherCondition.Unknown
}