package com.jsoft.f_test.core.database.weather

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDao {
    @Query("SELECT * FROM weather WHERE id = ${WeatherEntity.SINGLE_ROW_ID} LIMIT 1")
    fun observeCurrent(): Flow<WeatherEntity?>

    @Upsert
    suspend fun upsert(weather: WeatherEntity)

    @Query("DELETE FROM weather")
    suspend fun clear()
}