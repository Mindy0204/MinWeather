package com.mindyhsu.minweather.data.remote

import android.location.Location
import com.mindyhsu.minweather.model.Result

interface LocationDataSource {
    suspend fun getLastLocation(): Result<Location>
}