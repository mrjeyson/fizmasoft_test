package com.jsoft.f_test.domain.location.repository

import com.jsoft.f_test.core.common.result.AppResult
import com.jsoft.f_test.domain.location.model.Coordinates

interface LocationRepository {
    suspend fun getCurrentLocation(): AppResult<Coordinates>
}