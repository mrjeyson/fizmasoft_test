package com.jsoft.f_test.feature.movies.uistate

import com.jsoft.f_test.domain.movies.model.Movie
import java.util.Locale

internal fun Movie.toUi(): MovieUi = MovieUi(
    id = id.value,
    title = title,
    posterUrl = posterUrl,
    ratingText = String.format(Locale.ROOT, "%.1f", rating),
    releaseYearText = releaseYear?.toString() ?: "—",
    overview = overview,
)