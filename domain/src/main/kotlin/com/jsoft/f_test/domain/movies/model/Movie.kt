package com.jsoft.f_test.domain.movies.model

data class Movie(
    val id: MovieId,
    val title: String,
    val posterUrl: String?,
    val rating: Double,
    val releaseYear: Int?,
    val overview: String,
)

@JvmInline
value class MovieId(val value: Int)