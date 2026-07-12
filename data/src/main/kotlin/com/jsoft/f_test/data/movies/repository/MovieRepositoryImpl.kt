package com.jsoft.f_test.data.movies.repository

import com.jsoft.f_test.core.common.coroutine.DispatcherProvider
import com.jsoft.f_test.core.common.result.AppResult
import com.jsoft.f_test.core.database.movies.MovieDao
import com.jsoft.f_test.core.network.safeApiCall
import com.jsoft.f_test.data.movies.mapper.toDomain
import com.jsoft.f_test.data.movies.mapper.toEntity
import com.jsoft.f_test.data.movies.remote.MovieApi
import com.jsoft.f_test.domain.movies.model.Movie
import com.jsoft.f_test.domain.movies.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class MovieRepositoryImpl(
    private val api: MovieApi,
    private val dao: MovieDao,
    private val apiKey: String,
    private val dispatchers: DispatcherProvider,
) : MovieRepository {

    override fun observeMovies(): Flow<List<Movie>> {
        return dao.observeAll().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun refreshMovies(): AppResult<Unit> = withContext(dispatchers.io) {
        val apiResult = safeApiCall {
            api.getPopularMovies(apiKey = apiKey)
        }

        when (apiResult) {
            is AppResult.Success -> {
                val entities = apiResult.data.results.map { it.toEntity() }
                dao.upsertAll(entities)
                AppResult.Success(Unit)
            }

            is AppResult.Error -> apiResult
        }
    }
}