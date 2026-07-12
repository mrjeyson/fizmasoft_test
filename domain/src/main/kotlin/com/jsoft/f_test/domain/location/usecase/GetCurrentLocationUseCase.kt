package com.jsoft.f_test.domain.location.usecase

import com.jsoft.f_test.core.common.result.AppResult
import com.jsoft.f_test.domain.location.model.Coordinates
import com.jsoft.f_test.domain.location.repository.LocationRepository

class GetCurrentLocationUseCase(
    private val repository: LocationRepository,
) {
    suspend operator fun invoke(): AppResult<Coordinates> = repository.getCurrentLocation()
}