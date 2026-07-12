package com.jsoft.f_test.domain.movies.repository

import com.jsoft.f_test.core.common.result.AppResult
import com.jsoft.f_test.domain.movies.model.Movie
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    fun observeMovies(): Flow<List<Movie>>

    suspend fun refreshMovies(): AppResult<Unit>
}