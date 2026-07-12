package com.jsoft.f_test.data.movies.mapper

import com.jsoft.f_test.core.database.movies.MovieEntity
import com.jsoft.f_test.data.movies.remote.MovieDto
import com.jsoft.f_test.domain.movies.model.Movie
import com.jsoft.f_test.domain.movies.model.MovieId

private const val POSTER_BASE_URL = "https://image.tmdb.org/t/p/w500"

internal fun MovieDto.toEntity(): MovieEntity = MovieEntity(
    id = id,
    title = title,
    posterPath = posterPath,
    voteAverage = voteAverage,
    releaseDate = releaseDate,
    overview = overview,
)

internal fun MovieEntity.toDomain(): Movie = Movie(
    id = MovieId(id),
    title = title,
    posterUrl = posterPath?.let { POSTER_BASE_URL + it },
    rating = voteAverage,
    releaseYear = releaseDate?.take(4)?.toIntOrNull(),
    overview = overview,
)