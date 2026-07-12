package com.jsoft.f_test.domain.prayer.repository

import com.jsoft.f_test.core.common.result.AppResult
import com.jsoft.f_test.domain.location.model.Coordinates
import com.jsoft.f_test.domain.prayer.model.PrayerTimes
import kotlinx.coroutines.flow.Flow

interface PrayerRepository {
    fun observeTodayPrayerTimes(): Flow<PrayerTimes?>
    suspend fun getPrayerTimes(date: String): PrayerTimes?
    suspend fun refreshPrayerTimes(coordinates: Coordinates, date: String): AppResult<Unit>
}