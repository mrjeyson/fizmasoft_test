package com.jsoft.f_test.domain.auth.repository

import com.jsoft.f_test.core.common.result.AppResult
import com.jsoft.f_test.domain.auth.model.Credentials

interface AuthRepository {
    suspend fun login(credentials: Credentials): AppResult<Unit>
}