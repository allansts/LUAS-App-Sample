package com.example.luasapp.repository

import com.example.luasapp.api.utils.ResponseState
import com.example.luasapp.model.StopInfo
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class ForecastRepositoryImplTest {

    private val repositoryImpl = mockk<ForecastRepositoryImpl>(relaxed = true)

    private val stopInfo = StopInfo("2020-07-31T14:55:38",
        "Marlborough",
        "MAR",
        "Green Line services operating normally",
        mutableListOf()
    )

    private val successState = ResponseState.Success(stopInfo)
    private val errorState = ResponseState.Error(Throwable(), 404)

    @Before
    fun setUp() {}

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `should return trams forecast with success`() = runBlocking {
        coEvery { repositoryImpl.tramsForecast(stop = any()) } returns successState

        val result = repositoryImpl.tramsForecast("any")

        coVerify { repositoryImpl.tramsForecast(any()) }

        assertEquals(successState, result)
    }

    @Test
    fun `should failed when trams forecast is called`() = runBlocking {
        coEvery { repositoryImpl.tramsForecast(stop = any()) } returns errorState

        val result = repositoryImpl.tramsForecast("any")

        coVerify { repositoryImpl.tramsForecast(any()) }

        assertEquals(errorState, result)
    }
}