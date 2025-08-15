package com.mindyhsu.minweather.data.repository

import android.location.Location
import com.mindyhsu.minweather.model.Result
import com.mindyhsu.minweather.model.geo.LocationInfo

interface LocationRepository {
    suspend fun getLastLocation(): Result<Location>

    suspend fun searchPlace(locationName: String, limit: Int?): Result<List<LocationInfo>>
}