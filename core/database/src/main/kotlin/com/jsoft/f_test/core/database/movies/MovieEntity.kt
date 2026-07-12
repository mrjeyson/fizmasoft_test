package com.jsoft.f_test.core.database.movies

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movies")
data class MovieEntity(
    @PrimaryKey val id: Int,
    val title: String,
    val posterPath: String?,
    val voteAverage: Double,
    val releaseDate: String?,
    val overview: String,
    val cachedAt: Long = System.currentTimeMillis()
)