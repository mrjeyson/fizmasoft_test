package com.jsoft.f_test.domain.auth.usecase

import com.jsoft.f_test.core.common.result.AppResult
import com.jsoft.f_test.domain.auth.model.Credentials
import com.jsoft.f_test.domain.auth.repository.AuthRepository

class LoginUseCase(
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(credentials: Credentials): AppResult<Unit> {
        return authRepository.login(credentials)
    }
}