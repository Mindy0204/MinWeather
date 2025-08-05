package com.mindyhsu.minweather.di

import android.content.Context
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.mindyhsu.minweather.common.NetworkConstants.CONTENT_TYPE
import com.mindyhsu.minweather.common.NetworkConstants.WEATHER_URL
import com.mindyhsu.minweather.data.remote.LocationDataSource
import com.mindyhsu.minweather.data.remote.LocationDataSourceImpl
import com.mindyhsu.minweather.data.remote.WeatherApiService
import com.mindyhsu.minweather.data.remote.WeatherRemoteDataSource
import com.mindyhsu.minweather.data.remote.WeatherRemoteDataSourceImpl
import com.mindyhsu.minweather.utils.HttpClientUtil
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Singleton
    @Provides
    fun provideWeatherApi(): WeatherApiService {
        val json = Json { ignoreUnknownKeys = true }
        val retrofit = Retrofit.Builder()
            .baseUrl(WEATHER_URL)
            .addConverterFactory(json.asConverterFactory(CONTENT_TYPE.toMediaType()))
            .client(HttpClientUtil.createHttpClient())
            .build()

        return retrofit.create(WeatherApiService::class.java)
    }

    @Singleton
    @Provides
    fun provideRemoteDataSource(apiService: WeatherApiService): WeatherRemoteDataSource {
        return WeatherRemoteDataSourceImpl(apiService)
    }

    @Singleton
    @Provides
    fun provideLocationDataSource(@ApplicationContext context: Context): LocationDataSource {
        return LocationDataSourceImpl(context)
    }
}