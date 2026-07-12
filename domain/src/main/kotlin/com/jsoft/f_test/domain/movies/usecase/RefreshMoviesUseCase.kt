package com.jsoft.f_test.domain.movies.usecase

import com.jsoft.f_test.core.common.result.AppResult
import com.jsoft.f_test.domain.movies.repository.MovieRepository

class RefreshMoviesUseCase(
    private val repository: MovieRepository,
) {
    suspend operator fun invoke(): AppResult<Unit> = repository.refreshMovies()
}