package com.jsoft.f_test.di

import com.jsoft.f_test.feature.face.viewmodel.FaceViewModel
import com.jsoft.f_test.feature.login.viewmodel.LoginViewModel
import com.jsoft.f_test.feature.map.viewmodel.MapViewModel
import com.jsoft.f_test.feature.movies.viewmodel.MoviesViewModel
import com.jsoft.f_test.feature.prayer.viewmodel.PrayerViewModel
import com.jsoft.f_test.feature.weather.viewmodel.WeatherViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { LoginViewModel(loginUseCase = get()) }
    viewModel { FaceViewModel(faceDetectionEngine = get()) }
    viewModel { MoviesViewModel(getMovies = get(), refreshMovies = get()) }
    viewModel { WeatherViewModel(getWeather = get(), refreshWeather = get(), getCurrentLocation = get()) }
    viewModel { MapViewModel(getCurrentLocation = get()) }
    viewModel { PrayerViewModel(getTodayPrayerTimes = get(), refreshPrayerTimes = get(), getCurrentLocation = get(), schedulePrayerAlarms = get()) }
}