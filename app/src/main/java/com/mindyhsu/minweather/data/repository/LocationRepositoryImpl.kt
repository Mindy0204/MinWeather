package com.mindyhsu.minweather.data.repository

import android.location.Location
import com.mindyhsu.minweather.data.remote.LocationDataSource
import com.mindyhsu.minweather.model.Result
import javax.inject.Inject

class LocationRepositoryImpl @Inject constructor(
    private val locationDataSource: LocationDataSource
) : LocationRepository {
    override suspend fun getLastLocation(): Result<Location> {
        return locationDataSource.getLastLocation()
    }
}