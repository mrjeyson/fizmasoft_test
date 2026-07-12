package com.jsoft.f_test.data.prayer.remote

import kotlinx.serialization.Serializable

@Serializable
data class PrayerTimesResponse(
    val code: Int,
    val status: String,
    val data: PrayerDataDto,
)

@Serializable
data class PrayerDataDto(
    val timings: TimingsDto,
    val date: DateDto,
)

@Serializable
data class TimingsDto(
    val Fajr: String,
    val Dhuhr: String,
    val Asr: String,
    val Maghrib: String,
    val Isha: String,
)

@Serializable
data class DateDto(
    val timestamp: String,
    val readable: String,
)