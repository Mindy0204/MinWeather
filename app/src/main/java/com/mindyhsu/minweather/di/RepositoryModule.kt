package com.mindyhsu.minweather.di

import com.mindyhsu.minweather.data.remote.LocationDataSource
import com.mindyhsu.minweather.data.repository.WeatherRepository
import com.mindyhsu.minweather.data.repository.WeatherRepositoryImpl
import com.mindyhsu.minweather.data.remote.WeatherRemoteDataSource
import com.mindyhsu.minweather.data.repository.LocationRepository
import com.mindyhsu.minweather.data.repository.LocationRepositoryImpl
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

    @Singleton
    @Provides
    fun provideLocationRepository(
        locationDataSource: LocationDataSource
    ): LocationRepository {
        return LocationRepositoryImpl(locationDataSource)
    }
}