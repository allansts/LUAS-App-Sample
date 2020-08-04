package com.example.luasapp.repository

import com.example.luasapp.api.utils.ResponseState
import com.example.luasapp.model.StopInfo

interface ForecastRepository {
    suspend fun tramsForecast(stop: String): ResponseState<StopInfo>
}