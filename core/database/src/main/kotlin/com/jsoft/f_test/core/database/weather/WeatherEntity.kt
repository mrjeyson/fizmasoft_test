package com.jsoft.f_test.core.database.weather

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weather")
data class WeatherEntity(
    @PrimaryKey val id: Int = SINGLE_ROW_ID,
    val temperatureCelsius: Double,
    val weatherCode: Int,
    val windSpeedKmh: Double,
    val latitude: Double,
    val longitude: Double,
    val updatedAt: Long
) {
    companion object {
        const val SINGLE_ROW_ID = 0
    }
}