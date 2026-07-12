package com.jsoft.f_test.data.auth.repository

import com.jsoft.f_test.core.common.coroutine.DispatcherProvider
import com.jsoft.f_test.core.common.result.AppError
import com.jsoft.f_test.core.common.result.AppResult
import com.jsoft.f_test.data.auth.local.LocalCredentials
import com.jsoft.f_test.domain.auth.model.Credentials
import com.jsoft.f_test.domain.auth.repository.AuthRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import kotlin.time.Duration.Companion.milliseconds

class AuthRepositoryImpl(
    private val dispatchers: DispatcherProvider
) : AuthRepository {

    override suspend fun login(credentials: Credentials): AppResult<Unit> = withContext(dispatchers.io) {
        delay(500.milliseconds)

        val isDobCorrect = credentials.dateOfBirth == LocalCredentials.DATE_OF_BIRTH
        val isPassportCorrect = credentials.passport == LocalCredentials.PASSPORT

        if (isDobCorrect && isPassportCorrect) {
            AppResult.Success(Unit)
        } else {
            AppResult.Error(AppError.InvalidCredentials)
        }
    }
}