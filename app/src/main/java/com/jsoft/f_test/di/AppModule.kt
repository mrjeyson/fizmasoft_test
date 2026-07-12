package com.jsoft.f_test.di

import com.jsoft.f_test.BuildConfig
import com.jsoft.f_test.core.common.coroutine.DefaultDispatcherProvider
import com.jsoft.f_test.core.common.coroutine.DispatcherProvider
import com.jsoft.f_test.core.database.AppDatabase
import com.jsoft.f_test.core.database.DatabaseModule
import com.jsoft.f_test.core.network.NetworkModule
import com.jsoft.f_test.core.notification.AlarmSchedulerImpl
import com.jsoft.f_test.core.notification.NotificationHelper
import com.jsoft.f_test.data.auth.local.MlKitFaceDetectionEngine
import com.jsoft.f_test.domain.auth.repository.FaceDetectionEngine
import com.jsoft.f_test.domain.auth.usecase.LoginUseCase
import com.jsoft.f_test.domain.location.usecase.GetCurrentLocationUseCase
import com.jsoft.f_test.domain.movies.usecase.GetMoviesUseCase
import com.jsoft.f_test.domain.movies.usecase.RefreshMoviesUseCase
import com.jsoft.f_test.domain.prayer.scheduler.AlarmScheduler
import com.jsoft.f_test.domain.prayer.usecase.GetTodayPrayerTimesUseCase
import com.jsoft.f_test.domain.prayer.usecase.RefreshPrayerTimesUseCase
import com.jsoft.f_test.domain.prayer.usecase.SchedulePrayerAlarmsUseCase
import com.jsoft.f_test.domain.weather.usecase.GetWeatherUseCase
import com.jsoft.f_test.domain.weather.usecase.RefreshWeatherUseCase
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import org.koin.dsl.module

val appModule = module {
    single<DispatcherProvider> { DefaultDispatcherProvider() }
    single<Json> { NetworkModule.provideJson() }
    single<OkHttpClient> { NetworkModule.provideOkHttpClient(isDebug = BuildConfig.DEBUG) }
    single<AppDatabase> { DatabaseModule.provideDatabase(get()) }

    single { NotificationHelper(context = get()) }
    single<AlarmScheduler> { AlarmSchedulerImpl(context = get()) }

    factory<FaceDetectionEngine> { MlKitFaceDetectionEngine() }
    factory { LoginUseCase(authRepository = get()) }
    factory { GetMoviesUseCase(repository = get()) }
    factory { RefreshMoviesUseCase(repository = get()) }
    factory { GetCurrentLocationUseCase(repository = get()) }
    factory { GetWeatherUseCase(repository = get()) }
    factory { RefreshWeatherUseCase(repository = get()) }
    factory { GetTodayPrayerTimesUseCase(repository = get()) }
    factory { RefreshPrayerTimesUseCase(repository = get()) }
    factory { SchedulePrayerAlarmsUseCase(alarmScheduler = get()) }
}