package com.example.luasapp.di

import com.example.luasapp.api.LUASForecasting
import com.example.luasapp.repository.ForecastRepository
import com.example.luasapp.repository.ForecastRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@InstallIn(ApplicationComponent::class)
@Module
object AppModule {

    @Singleton
    @Provides
    fun provideApiService(): LUASForecasting {
        return LUASForecasting()
    }

    @Singleton
    @Provides
    fun provideForecastRepository(apiService: LUASForecasting): ForecastRepository {
        return ForecastRepositoryImpl(apiService)
    }

}