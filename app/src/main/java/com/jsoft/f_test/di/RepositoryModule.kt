package com.jsoft.f_test.di

import com.jsoft.f_test.BuildConfig
import com.jsoft.f_test.core.database.AppDatabase
import com.jsoft.f_test.core.database.movies.MovieDao
import com.jsoft.f_test.core.database.prayer.PrayerTimesDao
import com.jsoft.f_test.core.database.weather.WeatherDao
import com.jsoft.f_test.core.location.LocationDataSource
import com.jsoft.f_test.data.auth.repository.AuthRepositoryImpl
import com.jsoft.f_test.data.location.repository.LocationRepositoryImpl
import com.jsoft.f_test.data.movies.repository.MovieRepositoryImpl
import com.jsoft.f_test.data.prayer.repository.PrayerRepositoryImpl
import com.jsoft.f_test.data.weather.repository.WeatherRepositoryImpl
import com.jsoft.f_test.domain.auth.repository.AuthRepository
import com.jsoft.f_test.domain.location.repository.LocationRepository
import com.jsoft.f_test.domain.movies.repository.MovieRepository
import com.jsoft.f_test.domain.prayer.repository.PrayerRepository
import com.jsoft.f_test.domain.weather.repository.WeatherRepository
import org.koin.dsl.module

val repositoryModule = module {
    single<AuthRepository> { AuthRepositoryImpl(dispatchers = get()) }
    single<MovieRepository> { MovieRepositoryImpl(api = get(), dao = get(), apiKey = BuildConfig.TMDB_API_KEY, dispatchers = get()) }
    single<LocationRepository> { LocationRepositoryImpl(dataSource = get(), dispatchers = get()) }
    single<WeatherRepository> { WeatherRepositoryImpl(api = get(), dao = get(), dispatchers = get()) }
    single<PrayerRepository> { PrayerRepositoryImpl(api = get(), dao = get(), dispatchers = get()) }

    single { LocationDataSource(context = get()) }

    single<PrayerTimesDao> { get<AppDatabase>().prayerTimesDao() }
    single<WeatherDao> { get<AppDatabase>().weatherDao() }
    single<MovieDao> { get<AppDatabase>().movieDao() }

}