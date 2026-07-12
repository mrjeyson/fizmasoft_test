package com.jsoft.f_test.core.database.prayer

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "prayer_times")
data class PrayerTimesEntity(
    @PrimaryKey val date: String,
    val fajr: Long,
    val dhuhr: Long,
    val asr: Long,
    val maghrib: Long,
    val isha: Long,
    val cachedAt: Long = System.currentTimeMillis()
)