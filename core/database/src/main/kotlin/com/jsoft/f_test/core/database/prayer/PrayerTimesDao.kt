package com.jsoft.f_test.core.database.prayer

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface PrayerTimesDao {
    @Query("SELECT * FROM prayer_times WHERE date = :date LIMIT 1")
    fun observeByDate(date: String): Flow<PrayerTimesEntity?>

    @Query("SELECT * FROM prayer_times WHERE date = :date LIMIT 1")
    suspend fun getByDate(date: String): PrayerTimesEntity?

    @Upsert
    suspend fun upsert(entity: PrayerTimesEntity)

    @Query("DELETE FROM prayer_times WHERE date < :date")
    suspend fun deleteBeforeDate(date: String)
}