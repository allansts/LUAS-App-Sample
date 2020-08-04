package com.example.luasapp.repository

import com.example.luasapp.api.LUASForecasting
import com.example.luasapp.api.utils.ResponseState
import com.example.luasapp.api.utils.SafeCall.safeApiCall
import com.example.luasapp.model.StopInfo
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class ForecastRepositoryImpl(
    private val apiService: LUASForecasting,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
): ForecastRepository {

    override suspend fun tramsForecast(stop: String): ResponseState<StopInfo> {
        return safeApiCall(ioDispatcher) {
            apiService.tramsForecast(stopName = stop)
        }
    }
}