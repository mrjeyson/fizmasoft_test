package com.jsoft.f_test.domain.prayer.model

data class PrayerTimes(
    val date: String,
    val fajr: Long,
    val dhuhr: Long,
    val asr: Long,
    val maghrib: Long,
    val isha: Long,
) {
    fun asList(): List<Prayer> = listOf(
        Prayer(PrayerName.Fajr, fajr),
        Prayer(PrayerName.Dhuhr, dhuhr),
        Prayer(PrayerName.Asr, asr),
        Prayer(PrayerName.Maghrib, maghrib),
        Prayer(PrayerName.Isha, isha),
    )
}

data class Prayer(
    val name: PrayerName,
    val timeMillis: Long,
)

enum class PrayerName {
    Fajr, Dhuhr, Asr, Maghrib, Isha,
}