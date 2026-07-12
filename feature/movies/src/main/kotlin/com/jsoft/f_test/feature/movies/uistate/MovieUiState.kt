package com.jsoft.f_test.feature.movies.uistate

data class MoviesUiState(
    val movies: List<MovieUi> = emptyList(),
    val isRefreshing: Boolean = false,
    val errorMessage: String? = null,
) {
    val isInitialLoading: Boolean get() = isRefreshing && movies.isEmpty()

    val isEmpty: Boolean get() = movies.isEmpty() && !isRefreshing && errorMessage == null
}

data class MovieUi(
    val id: Int,
    val title: String,
    val posterUrl: String?,
    val ratingText: String,
    val releaseYearText: String,
    val overview: String,
)