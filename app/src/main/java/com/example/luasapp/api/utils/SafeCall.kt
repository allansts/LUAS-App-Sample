package com.example.luasapp.api.utils

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException

object SafeCall {

    suspend fun <S : Any> safeApiCall(dispatcher: CoroutineDispatcher, apiCall: suspend () -> S
    ): ResponseState<S> {
        return withContext(dispatcher) {
            try {
                ResponseState.Success(apiCall.invoke())
            } catch (throwable: Throwable) {
                errorHandler(throwable)
            }
        }
    }

    private fun errorHandler(throwable: Throwable): ResponseState.Error {
        return when (throwable) {
            is HttpException -> ResponseState.Error(throwable, throwable.code())
            is SocketTimeoutException -> ResponseState.Error(throwable, 504)
            is ConnectException -> ResponseState.Error(throwable, 404)
            else -> ResponseState.Error(null, 404)
        }
    }
}