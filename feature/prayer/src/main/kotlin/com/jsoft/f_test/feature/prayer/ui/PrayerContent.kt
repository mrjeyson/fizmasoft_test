package com.jsoft.f_test.feature.prayer.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOff
import androidx.compose.material.icons.filled.Mosque
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jsoft.f_test.core.ui.theme.F_testTheme
import com.jsoft.f_test.feature.prayer.uistate.PrayerUi
import com.jsoft.f_test.feature.prayer.uistate.PrayerUiState

@Composable
fun PrayerContent(
    state: PrayerUiState,
    onRefresh: () -> Unit,
    onRequestPermission: () -> Unit,
    onErrorDismissed: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(state.errorMessage) {
        if (state.errorMessage != null && state.prayers.isNotEmpty()) {
            snackbarHostState.showSnackbar(state.errorMessage)
            onErrorDismissed()
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        when {
            state.permissionRequired -> PermissionView(onRequestPermission)
            state.isInitialLoading -> CenteredLoading()
            state.prayers.isEmpty() && state.errorMessage != null -> {
                ErrorView(state.errorMessage, onRefresh)
            }

            state.prayers.isEmpty() -> EmptyView(onRefresh)
            else -> PrayerList(
                state = state,
                isRefreshing = state.isRefreshing,
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
        Text("Joylashuvga ruxsat kerak", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(8.dp))
        Text(
            text = "Namoz vaqtlari joylashuvingizga qarab hisoblanadi.",
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
        Text("Xatolik", style = MaterialTheme.typography.titleMedium)
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
        Icon(
            imageVector = Icons.Filled.Mosque,
            contentDescription = null,
            modifier = Modifier.height(64.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(Modifier.height(16.dp))
        Text("Ma'lumot topilmadi", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(16.dp))
        Button(onClick = onRefresh) { Text("Yuklash") }
    }
}

@Composable
private fun PrayerList(
    state: PrayerUiState,
    isRefreshing: Boolean,
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = state.dateText,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            if (state.nextPrayerIndex != null) {
                Spacer(Modifier.height(4.dp))
                val nextPrayer = state.prayers[state.nextPrayerIndex]
                Text(
                    text = "Keyingi namoz: ${nextPrayer.name}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary,
                )
            }
        }

        Box(modifier = Modifier.fillMaxSize()) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                itemsIndexed(state.prayers) { index, prayer ->
                    PrayerRow(
                        prayer = prayer,
                        isNext = index == state.nextPrayerIndex,
                    )
                }
            }

            if (isRefreshing) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = 8.dp)
                        .height(24.dp),
                    strokeWidth = 2.dp,
                )
            }
        }
    }
}

@Composable
private fun PrayerRow(
    prayer: PrayerUi,
    isNext: Boolean,
) {
    val backgroundColor = when {
        isNext -> MaterialTheme.colorScheme.primaryContainer
        prayer.isPast -> Color.Transparent
        else -> Color.Transparent
    }

    val textColor = when {
        isNext -> MaterialTheme.colorScheme.onPrimaryContainer
        prayer.isPast -> MaterialTheme.colorScheme.onSurfaceVariant
        else -> MaterialTheme.colorScheme.onSurface
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(backgroundColor),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isNext) 4.dp else 1.dp,
        ),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = prayer.name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = if (isNext) FontWeight.Bold else FontWeight.Normal,
                color = textColor,
            )
            Text(
                text = prayer.timeText,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = if (isNext) FontWeight.Bold else FontWeight.Normal,
                color = textColor,
            )
        }
    }
}

private val samplePrayers = listOf(
    PrayerUi("Bomdod", "03:45", isPast = true),
    PrayerUi("Peshin", "12:30", isPast = true),
    PrayerUi("Asr", "17:15", isPast = false),
    PrayerUi("Shom", "19:40", isPast = false),
    PrayerUi("Xufton", "21:15", isPast = false),
)

@Preview(showBackground = true)
@Composable
private fun PrayerListPreview() {
    F_testTheme {
        PrayerContent(
            state = PrayerUiState(
                prayers = samplePrayers,
                dateText = "8 iyul, chorshanba",
                nextPrayerIndex = 2,
            ),
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
        PrayerContent(
            state = PrayerUiState(permissionRequired = true),
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
        PrayerContent(
            state = PrayerUiState(isRefreshing = true),
            onRefresh = {},
            onRequestPermission = {},
            onErrorDismissed = {},
        )
    }
}