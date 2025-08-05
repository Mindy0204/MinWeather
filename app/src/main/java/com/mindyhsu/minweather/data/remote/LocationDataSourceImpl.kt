package com.mindyhsu.minweather.data.remote

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import com.google.android.gms.location.LocationServices
import com.mindyhsu.minweather.model.Result
import dagger.hilt.android.qualifiers.ApplicationContext
import timber.log.Timber
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class LocationDataSourceImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : LocationDataSource {

    @SuppressLint("MissingPermission")
    override suspend fun getLastLocation(): Result<Location> = suspendCoroutine { continuation ->
        val client = LocationServices.getFusedLocationProviderClient(context)
        client.lastLocation
            .addOnSuccessListener {
                continuation.resume(Result.Success(it))
            }
            .addOnFailureListener {
                Timber.d("[LocationDataSourceImpl] getLastLocation error: $it")
                continuation.resume(Result.Error(it))
            }
    }
}