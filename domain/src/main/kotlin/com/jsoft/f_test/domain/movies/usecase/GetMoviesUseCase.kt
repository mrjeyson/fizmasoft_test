package com.jsoft.f_test.domain.movies.usecase

import com.jsoft.f_test.domain.movies.model.Movie
import com.jsoft.f_test.domain.movies.repository.MovieRepository
import kotlinx.coroutines.flow.Flow

class GetMoviesUseCase(
    private val repository: MovieRepository,
) {
    operator fun invoke(): Flow<List<Movie>> = repository.observeMovies()
}