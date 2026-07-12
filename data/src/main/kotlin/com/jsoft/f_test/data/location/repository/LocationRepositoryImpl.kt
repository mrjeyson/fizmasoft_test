package com.jsoft.f_test.data.location.repository

import com.jsoft.f_test.core.common.coroutine.DispatcherProvider
import com.jsoft.f_test.core.common.result.AppError
import com.jsoft.f_test.core.common.result.AppResult
import com.jsoft.f_test.core.location.LocationDataSource
import com.jsoft.f_test.domain.location.model.Coordinates
import com.jsoft.f_test.domain.location.repository.LocationRepository
import kotlinx.coroutines.withContext

class LocationRepositoryImpl(
    private val dataSource: LocationDataSource,
    private val dispatchers: DispatcherProvider,
) : LocationRepository {

    override suspend fun getCurrentLocation(): AppResult<Coordinates> =
        withContext(dispatchers.io) {
            if (!dataSource.hasLocationPermission()) {
                return@withContext AppResult.Error(AppError.PermissionDenied)
            }
            if (!dataSource.isLocationEnabled()) {
                return@withContext AppResult.Error(AppError.LocationDisabled)
            }

            try {
                val current = dataSource.getCurrentLocation()
                if (current != null) {
                    return@withContext AppResult.Success(current.toCoordinates())
                }

                val last = dataSource.getLastKnownLocation()
                if (last != null) {
                    return@withContext AppResult.Success(last.toCoordinates())
                }

                AppResult.Error(AppError.NotFound)
            } catch (e: SecurityException) {
                AppResult.Error(AppError.PermissionDenied)
            } catch (e: Exception) {
                AppResult.Error(AppError.Unknown(e))
            }
        }
}

private fun android.location.Location.toCoordinates(): Coordinates =
    Coordinates(latitude = latitude, longitude = longitude)