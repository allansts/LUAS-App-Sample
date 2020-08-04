package com.example.luasapp.api.utils

sealed class ResponseState<out S : Any> {
    /**
     * Success response with body
     */
    data class Success<S : Any>(val data: S) : ResponseState<S>()

    /**
     * Error with throwable? and code?
     */
    data class Error(val error: Throwable?, val code: Int?) : ResponseState<Nothing>()
}
