package com.jsoft.f_test.feature.prayer.uistate

import com.jsoft.f_test.domain.prayer.model.PrayerName
import com.jsoft.f_test.domain.prayer.model.PrayerTimes
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

internal fun PrayerTimes.toUiList(now: Long = System.currentTimeMillis()): List<PrayerUi> {
    return asList().map { prayer ->
        PrayerUi(
            name = prayer.name.toDisplayName(),
            timeText = formatTime(prayer.timeMillis),
            isPast = prayer.timeMillis < now,
        )
    }
}

internal fun PrayerTimes.nextPrayerIndex(now: Long = System.currentTimeMillis()): Int? {
    val list = asList()
    val index = list.indexOfFirst { it.timeMillis > now }
    return if (index == -1) null else index
}

internal fun formatDisplayDate(isoDate: String): String {
    val parser = SimpleDateFormat("yyyy-MM-dd", Locale.US)
    val formatter = SimpleDateFormat("d MMMM, EEEE", Locale("uz"))
    return try {
        parser.parse(isoDate)?.let { formatter.format(it) } ?: isoDate
    } catch (e: Exception) {
        isoDate
    }
}

private fun PrayerName.toDisplayName(): String = when (this) {
    PrayerName.Fajr -> "Bomdod"
    PrayerName.Dhuhr -> "Peshin"
    PrayerName.Asr -> "Asr"
    PrayerName.Maghrib -> "Shom"
    PrayerName.Isha -> "Xufton"
}

private fun formatTime(millis: Long): String {
    val formatter = SimpleDateFormat("HH:mm", Locale.US)
    return formatter.format(Date(millis))
}