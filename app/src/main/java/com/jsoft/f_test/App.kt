package com.jsoft.f_test

import android.app.Application
import com.jsoft.f_test.di.appModule
import com.jsoft.f_test.di.networkModule
import com.jsoft.f_test.di.repositoryModule
import com.jsoft.f_test.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(appModule, viewModelModule, networkModule, repositoryModule)
        }
    }
}