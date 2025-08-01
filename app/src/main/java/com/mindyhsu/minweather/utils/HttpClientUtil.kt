package com.mindyhsu.minweather.utils

import com.mindyhsu.minweather.BuildConfig
import com.mindyhsu.minweather.common.NetworkConstants.QUERY_PARAM_API_KEY
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object HttpClientUtil {

    @Singleton
    @Provides
    fun createHttpClient(): OkHttpClient {
        val logging: HttpLoggingInterceptor =
            HttpLoggingInterceptor().setLevel(
                if (BuildConfig.TIMBER_VISIABLE) {
                    HttpLoggingInterceptor.Level.BODY
                } else {
                    HttpLoggingInterceptor.Level.NONE
                }
            )

        val authInterceptor = Interceptor { chain ->
            val original = chain.request()
            val newUrl = original.url.newBuilder()
                .addQueryParameter(QUERY_PARAM_API_KEY, BuildConfig.WEATHER_API_KEY)
                .build()
            val newRequest = original.newBuilder()
                .url(newUrl)
                .build()

            chain.proceed(newRequest)
        }

        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .addInterceptor(authInterceptor)
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .build()
    }
}