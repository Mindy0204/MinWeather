package com.mindyhsu.minweather.di

import com.mindyhsu.minweather.data.WeatherRepository
import com.mindyhsu.minweather.data.WeatherRepositoryImpl
import com.mindyhsu.minweather.data.remote.WeatherRemoteDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun provideWeatherRepository(
        remoteDataSource: WeatherRemoteDataSource
    ): WeatherRepository {
        return WeatherRepositoryImpl(remoteDataSource)
    }
}