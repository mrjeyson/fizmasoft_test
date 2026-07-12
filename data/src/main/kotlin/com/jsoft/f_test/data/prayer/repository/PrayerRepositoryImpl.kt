package com.jsoft.f_test.data.prayer.repository

import com.jsoft.f_test.core.common.coroutine.DispatcherProvider
import com.jsoft.f_test.core.common.result.AppResult
import com.jsoft.f_test.core.database.prayer.PrayerTimesDao
import com.jsoft.f_test.core.network.safeApiCall
import com.jsoft.f_test.data.prayer.mapper.toDomain
import com.jsoft.f_test.data.prayer.mapper.toEntity
import com.jsoft.f_test.data.prayer.remote.PrayerApi
import com.jsoft.f_test.domain.location.model.Coordinates
import com.jsoft.f_test.domain.prayer.model.PrayerTimes
import com.jsoft.f_test.domain.prayer.repository.PrayerRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PrayerRepositoryImpl(
    private val api: PrayerApi,
    private val dao: PrayerTimesDao,
    private val dispatchers: DispatcherProvider,
) : PrayerRepository {

    override fun observeTodayPrayerTimes(): Flow<PrayerTimes?> {
        val today = todayIsoDate()
        return dao.observeByDate(today).map { entity -> entity?.toDomain() }
    }

    override suspend fun getPrayerTimes(date: String): PrayerTimes? =
        withContext(dispatchers.io) {
            dao.getByDate(date)?.toDomain()
        }

    override suspend fun refreshPrayerTimes(
        coordinates: Coordinates,
        date: String,
    ): AppResult<Unit> = withContext(dispatchers.io) {
        val apiResult = safeApiCall {
            api.getTimings(
                date = date.toAladhanFormat(),
                latitude = coordinates.latitude,
                longitude = coordinates.longitude,
            )
        }

        when (apiResult) {
            is AppResult.Success -> {
                val entity = apiResult.data.toEntity(date = date)
                dao.upsert(entity)
                AppResult.Success(Unit)
            }

            is AppResult.Error -> apiResult
        }
    }

    private companion object {
        private val isoFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        private val aladhanFormatter = SimpleDateFormat("dd-MM-yyyy", Locale.US)

        fun todayIsoDate(): String = isoFormatter.format(Date())

        fun String.toAladhanFormat(): String {
            val date = isoFormatter.parse(this) ?: return this
            return aladhanFormatter.format(date)
        }
    }
}