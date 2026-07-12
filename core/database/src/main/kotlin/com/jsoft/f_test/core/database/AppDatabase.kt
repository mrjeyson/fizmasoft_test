package com.jsoft.f_test.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.jsoft.f_test.core.database.movies.MovieDao
import com.jsoft.f_test.core.database.movies.MovieEntity
import com.jsoft.f_test.core.database.prayer.PrayerTimesDao
import com.jsoft.f_test.core.database.prayer.PrayerTimesEntity
import com.jsoft.f_test.core.database.weather.WeatherDao
import com.jsoft.f_test.core.database.weather.WeatherEntity

@Database(
    entities = [
        MovieEntity::class,
        WeatherEntity::class,
        PrayerTimesEntity::class
    ],
    version = 1,
    exportSchema = false,
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao
    abstract fun weatherDao(): WeatherDao
    abstract fun prayerTimesDao(): PrayerTimesDao
}

const val DATABASE_NAME = "f_test.db"