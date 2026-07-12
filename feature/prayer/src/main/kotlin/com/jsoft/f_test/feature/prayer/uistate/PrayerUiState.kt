package com.jsoft.f_test.feature.prayer.uistate

data class PrayerUiState(
    val prayers: List<PrayerUi> = emptyList(),
    val dateText: String = "",
    val nextPrayerIndex: Int? = null,
    val isRefreshing: Boolean = false,
    val errorMessage: String? = null,
    val permissionRequired: Boolean = false,
) {
    val isInitialLoading: Boolean get() = isRefreshing && prayers.isEmpty()
}

data class PrayerUi(
    val name: String,
    val timeText: String,
    val isPast: Boolean,
)