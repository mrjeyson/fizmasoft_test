package com.jsoft.f_test.data.prayer.mapper

import com.jsoft.f_test.core.database.prayer.PrayerTimesEntity
import com.jsoft.f_test.data.prayer.remote.PrayerTimesResponse
import com.jsoft.f_test.domain.prayer.model.PrayerTimes
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

internal fun PrayerTimesResponse.toEntity(date: String): PrayerTimesEntity {
    val timings = data.timings
    val dateMillis = date.toDateMillisUtc()

    return PrayerTimesEntity(
        date = date,
        fajr = timings.Fajr.toTimeMillis(dateMillis),
        dhuhr = timings.Dhuhr.toTimeMillis(dateMillis),
        asr = timings.Asr.toTimeMillis(dateMillis),
        maghrib = timings.Maghrib.toTimeMillis(dateMillis),
        isha = timings.Isha.toTimeMillis(dateMillis),
    )
}

internal fun PrayerTimesEntity.toDomain(): PrayerTimes = PrayerTimes(
    date = date,
    fajr = fajr,
    dhuhr = dhuhr,
    asr = asr,
    maghrib = maghrib,
    isha = isha,
)

private fun String.toDateMillisUtc(): Long {
    val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.US).apply {
        timeZone = TimeZone.getTimeZone("UTC")
    }
    return formatter.parse(this)?.time ?: System.currentTimeMillis()
}

private fun String.toTimeMillis(dateMillisUtc: Long): Long {
    val cleaned = this.split(" ").first()
    val parts = cleaned.split(":")
    if (parts.size != 2) return dateMillisUtc

    val hour = parts[0].toIntOrNull() ?: return dateMillisUtc
    val minute = parts[1].toIntOrNull() ?: return dateMillisUtc

    val calendar = Calendar.getInstance(TimeZone.getDefault()).apply {
        val dateStr = SimpleDateFormat("yyyy-MM-dd", Locale.US).apply {
            timeZone = TimeZone.getTimeZone("UTC")
        }.format(Date(dateMillisUtc))

        val localFormatter = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US).apply {
            timeZone = TimeZone.getDefault()
        }
        val fullDateTime = localFormatter.parse("$dateStr ${"%02d:%02d".format(hour, minute)}")
        time = fullDateTime ?: Date()
    }

    return calendar.timeInMillis
}