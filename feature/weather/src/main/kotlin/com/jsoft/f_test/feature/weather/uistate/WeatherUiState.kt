package com.jsoft.f_test.feature.weather.uistate

data class WeatherUiState(
    val weather: WeatherUi? = null,
    val isRefreshing: Boolean = false,
    val errorMessage: String? = null,
    val permissionRequired: Boolean = false,
) {
    val isInitialLoading: Boolean get() = isRefreshing && weather == null
}

data class WeatherUi(
    val temperatureText: String,
    val conditionText: String,
    val conditionIcon: WeatherIcon,
    val windText: String,
    val updatedText: String,
)

enum class WeatherIcon(val emoji: String) {
    Sunny("☀️"),
    PartlyCloudy("⛅"),
    Cloudy("☁️"),
    Foggy("🌫️"),
    Rainy("🌧️"),
    Snowy("❄️"),
    Stormy("⛈️"),
    Unknown("❓"),
}