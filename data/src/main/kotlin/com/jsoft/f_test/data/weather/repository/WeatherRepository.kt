package com.jsoft.f_test.data.weather.repository

import com.jsoft.f_test.core.common.coroutine.DispatcherProvider
import com.jsoft.f_test.core.common.result.AppResult
import com.jsoft.f_test.core.database.weather.WeatherDao
import com.jsoft.f_test.core.network.safeApiCall
import com.jsoft.f_test.data.weather.mapper.toDomain
import com.jsoft.f_test.data.weather.mapper.toEntity
import com.jsoft.f_test.data.weather.remote.WeatherApi
import com.jsoft.f_test.domain.location.model.Coordinates
import com.jsoft.f_test.domain.weather.model.Weather
import com.jsoft.f_test.domain.weather.repository.WeatherRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class WeatherRepositoryImpl(
    private val api: WeatherApi,
    private val dao: WeatherDao,
    private val dispatchers: DispatcherProvider,
) : WeatherRepository {

    override fun observeWeather(): Flow<Weather?> {
        return dao.observeCurrent().map { entity -> entity?.toDomain() }
    }

    override suspend fun refreshWeather(coordinates: Coordinates): AppResult<Unit> =
        withContext(dispatchers.io) {
            val apiResult = safeApiCall {
                api.getCurrentWeather(
                    latitude = coordinates.latitude,
                    longitude = coordinates.longitude,
                )
            }

            when (apiResult) {
                is AppResult.Success -> {
                    dao.upsert(apiResult.data.toEntity())
                    AppResult.Success(Unit)
                }

                is AppResult.Error -> apiResult
            }
        }
}