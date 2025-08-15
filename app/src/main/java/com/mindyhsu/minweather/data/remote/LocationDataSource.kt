package com.mindyhsu.minweather.data.remote

import android.location.Location
import com.mindyhsu.minweather.model.Result
import com.mindyhsu.minweather.model.geo.LocationInfo

interface LocationDataSource {
    suspend fun getLastLocation(): Result<Location>

    suspend fun fetchLocationInfo(locationName: String, limit: Int?): List<LocationInfo>
}