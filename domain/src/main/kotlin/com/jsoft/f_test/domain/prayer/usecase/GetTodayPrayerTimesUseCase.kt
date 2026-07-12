package com.jsoft.f_test.domain.prayer.usecase

import com.jsoft.f_test.domain.prayer.model.PrayerTimes
import com.jsoft.f_test.domain.prayer.repository.PrayerRepository
import kotlinx.coroutines.flow.Flow

class GetTodayPrayerTimesUseCase(
    private val repository: PrayerRepository,
) {
    operator fun invoke(): Flow<PrayerTimes?> = repository.observeTodayPrayerTimes()
}