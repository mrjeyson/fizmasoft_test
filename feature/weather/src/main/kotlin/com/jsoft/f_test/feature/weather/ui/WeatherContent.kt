package com.jsoft.f_test.feature.weather.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOff
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jsoft.f_test.core.ui.theme.F_testTheme
import com.jsoft.f_test.feature.weather.uistate.WeatherIcon
import com.jsoft.f_test.feature.weather.uistate.WeatherUi
import com.jsoft.f_test.feature.weather.uistate.WeatherUiState

@Composable
fun WeatherContent(
    state: WeatherUiState,
    onRefresh: () -> Unit,
    onRequestPermission: () -> Unit,
    onErrorDismissed: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(state.errorMessage) {
        if (state.errorMessage != null && state.weather != null) {
            snackbarHostState.showSnackbar(state.errorMessage)
            onErrorDismissed()
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        when {
            state.permissionRequired -> PermissionView(onRequestPermission)
            state.isInitialLoading -> CenteredLoading()
            state.weather == null && state.errorMessage != null -> {
                ErrorView(state.errorMessage, onRefresh)
            }

            state.weather == null -> EmptyView(onRefresh)
            else -> WeatherCard(
                weather = state.weather,
                isRefreshing = state.isRefreshing,
                onRefresh = onRefresh,
            )
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter),
        )
    }
}

@Composable
private fun CenteredLoading() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

@Composable
private fun PermissionView(onRequestPermission: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(
            imageVector = Icons.Filled.LocationOff,
            contentDescription = null,
            modifier = Modifier.height(64.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(Modifier.height(16.dp))
        Text(
            text = "Joylashuvga ruxsat kerak",
            style = MaterialTheme.typography.titleMedium,
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = "Ob-havo ma'lumotlarini ko'rish uchun joylashuvingizga ruxsat bering.",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(Modifier.height(24.dp))
        Button(onClick = onRequestPermission) { Text("Ruxsat berish") }
    }
}

@Composable
private fun ErrorView(message: String, onRetry: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "Xatolik",
            style = MaterialTheme.typography.titleMedium,
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(16.dp))
        Button(onClick = onRetry) { Text("Qayta urinish") }
    }
}

@Composable
private fun EmptyView(onRefresh: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "Ma'lumot yo'q",
            style = MaterialTheme.typography.titleMedium,
        )
        Spacer(Modifier.height(16.dp))
        Button(onClick = onRefresh) { Text("Yuklash") }
    }
}

@Composable
private fun WeatherCard(
    weather: WeatherUi,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
        ) {
            IconButton(onClick = onRefresh, enabled = !isRefreshing) {
                if (isRefreshing) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .height(20.dp)
                            .width(20.dp),
                        strokeWidth = 2.dp,
                    )
                } else {
                    Icon(Icons.Filled.Refresh, contentDescription = "Yangilash")
                }
            }
        }

        Spacer(Modifier.height(32.dp))

        Text(
            text = weather.conditionIcon.emoji,
            fontSize = 96.sp,
        )

        Spacer(Modifier.height(16.dp))

        Text(
            text = weather.temperatureText,
            fontSize = 64.sp,
            style = MaterialTheme.typography.displayLarge,
        )

        Text(
            text = weather.conditionText,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        Spacer(Modifier.height(32.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = weather.windText,
                    style = MaterialTheme.typography.bodyLarge,
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = weather.updatedText,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

private val sampleWeather = WeatherUi(
    temperatureText = "25°C",
    conditionText = "Ochiq havo",
    conditionIcon = WeatherIcon.Sunny,
    windText = "Shamol: 12.5 km/soat",
    updatedText = "5 daqiqa oldin yangilandi",
)

@Preview(showBackground = true)
@Composable
private fun WeatherCardPreview() {
    F_testTheme {
        WeatherContent(
            state = WeatherUiState(weather = sampleWeather),
            onRefresh = {},
            onRequestPermission = {},
            onErrorDismissed = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PermissionPreview() {
    F_testTheme {
        WeatherContent(
            state = WeatherUiState(permissionRequired = true),
            onRefresh = {},
            onRequestPermission = {},
            onErrorDismissed = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun LoadingPreview() {
    F_testTheme {
        WeatherContent(
            state = WeatherUiState(isRefreshing = true),
            onRefresh = {},
            onRequestPermission = {},
            onErrorDismissed = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ErrorPreview() {
    F_testTheme {
        WeatherContent(
            state = WeatherUiState(errorMessage = "Internet aloqasi yo'q"),
            onRefresh = {},
            onRequestPermission = {},
            onErrorDismissed = {},
        )
    }
}