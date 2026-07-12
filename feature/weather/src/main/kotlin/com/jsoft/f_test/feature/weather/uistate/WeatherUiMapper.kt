package com.jsoft.f_test.feature.weather.uistate

import com.jsoft.f_test.domain.weather.model.Weather
import com.jsoft.f_test.domain.weather.model.WeatherCondition
import java.util.Locale
import kotlin.math.roundToInt

internal fun Weather.toUi(): WeatherUi = WeatherUi(
    temperatureText = "${temperatureCelsius.roundToInt()}°C",
    conditionText = condition.toDisplayName(),
    conditionIcon = condition.toIcon(),
    windText = "Shamol: ${String.format(Locale.ROOT, "%.1f", windSpeedKmh)} km/soat",
    updatedText = formatRelativeTime(updatedAt),
)

private fun WeatherCondition.toDisplayName(): String = when (this) {
    WeatherCondition.Clear -> "Ochiq havo"
    WeatherCondition.PartlyCloudy -> "Qisman bulutli"
    WeatherCondition.Cloudy -> "Bulutli"
    WeatherCondition.Fog -> "Tumanli"
    WeatherCondition.Drizzle -> "Yomg'ir sepalayapti"
    WeatherCondition.Rain -> "Yomg'ir"
    WeatherCondition.Snow -> "Qor"
    WeatherCondition.Thunderstorm -> "Momaqaldiroq"
    WeatherCondition.Unknown -> "Noma'lum"
}

private fun WeatherCondition.toIcon(): WeatherIcon = when (this) {
    WeatherCondition.Clear -> WeatherIcon.Sunny
    WeatherCondition.PartlyCloudy -> WeatherIcon.PartlyCloudy
    WeatherCondition.Cloudy -> WeatherIcon.Cloudy
    WeatherCondition.Fog -> WeatherIcon.Foggy
    WeatherCondition.Drizzle, WeatherCondition.Rain -> WeatherIcon.Rainy
    WeatherCondition.Snow -> WeatherIcon.Snowy
    WeatherCondition.Thunderstorm -> WeatherIcon.Stormy
    WeatherCondition.Unknown -> WeatherIcon.Unknown
}

private fun formatRelativeTime(timestampMillis: Long): String {
    val now = System.currentTimeMillis()
    val diffSeconds = (now - timestampMillis) / 1000
    return when {
        diffSeconds < 60 -> "Hozir yangilandi"
        diffSeconds < 3600 -> "${diffSeconds / 60} daqiqa oldin yangilandi"
        diffSeconds < 86400 -> "${diffSeconds / 3600} soat oldin yangilandi"
        else -> "${diffSeconds / 86400} kun oldin yangilandi"
    }
}