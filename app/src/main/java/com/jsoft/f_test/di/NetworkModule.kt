package com.jsoft.f_test.di

import com.jsoft.f_test.core.network.NetworkModule
import com.jsoft.f_test.data.movies.remote.MovieApi
import com.jsoft.f_test.data.prayer.remote.PrayerApi
import com.jsoft.f_test.data.weather.remote.WeatherApi
import org.koin.dsl.module

val networkModule = module {

    single<MovieApi> {
        NetworkModule.provideRetrofit(
            baseUrl = "https://api.themoviedb.org/3/",
            okHttpClient = get(),
            json = get(),
        ).create(MovieApi::class.java)
    }

    single<PrayerApi> {
        NetworkModule.provideRetrofit(
            baseUrl = "https://api.aladhan.com/v1/",
            okHttpClient = get(),
            json = get(),
        ).create(PrayerApi::class.java)
    }

    single<WeatherApi> {
        NetworkModule.provideRetrofit(
            baseUrl = "https://api.open-meteo.com/v1/",
            okHttpClient = get(),
            json = get(),
        ).create(WeatherApi::class.java)
    }
}