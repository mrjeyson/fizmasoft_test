package com.jsoft.f_test.domain.prayer.usecase

import com.jsoft.f_test.core.common.result.AppResult
import com.jsoft.f_test.domain.location.model.Coordinates
import com.jsoft.f_test.domain.prayer.repository.PrayerRepository
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class RefreshPrayerTimesUseCase(
    private val repository: PrayerRepository,
) {
    suspend operator fun invoke(coordinates: Coordinates): AppResult<Unit> {
        val today = dateFormatter.format(Date())
        return repository.refreshPrayerTimes(coordinates, today)
    }

    companion object {
        private val dateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.US)
    }
}