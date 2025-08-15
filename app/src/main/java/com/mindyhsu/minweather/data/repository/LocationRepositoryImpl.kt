package com.mindyhsu.minweather.data.repository

import android.location.Location
import com.mindyhsu.minweather.data.remote.LocationDataSource
import com.mindyhsu.minweather.model.Result
import com.mindyhsu.minweather.model.geo.LocationInfo
import timber.log.Timber
import javax.inject.Inject

class LocationRepositoryImpl @Inject constructor(
    private val locationDataSource: LocationDataSource
) : LocationRepository {
    override suspend fun getLastLocation(): Result<Location> {
        return locationDataSource.getLastLocation()
    }

    override suspend fun searchPlace(
        locationName: String,
        limit: Int?
    ): Result<List<LocationInfo>> {
        return try {
            val info = locationDataSource.fetchLocationInfo(locationName, limit)
            Result.Success(info)
        } catch (e: Exception) {
            Timber.d("[LocationRepositoryImpl] getLocationInfo from remote error: $e")
            Result.Error(e)
        }
    }
}