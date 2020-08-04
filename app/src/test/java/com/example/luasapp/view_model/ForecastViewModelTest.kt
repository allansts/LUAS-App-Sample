package com.example.luasapp.view_model

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.luasapp.api.utils.ResponseState
import com.example.luasapp.extensions.toDate
import com.example.luasapp.model.StopInfo
import com.example.luasapp.repository.ForecastRepositoryImpl
import com.example.luasapp.ui.forecast.ForecastViewModel
import com.example.luasapp.utils.DatePatterns
import com.example.luasapp.utils.InstantExecutorExtension
import com.example.luasapp.utils.LuasStop
import com.example.luasapp.utils.State
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.unmockkAll
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExperimentalCoroutinesApi
@ExtendWith(InstantExecutorExtension::class)
class ForecastViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private val repository = mockk<ForecastRepositoryImpl>(relaxed = true)
    private val observer = mockk<Observer<State<StopInfo>>>(relaxed = true)
    private lateinit var viewModel: ForecastViewModel

    private val testDispatcher = TestCoroutineDispatcher()

    private val marStopInfo = StopInfo("2020-07-31T14:55:38",
        "Marlborough",
        "MAR",
        "Green Line services operating normally",
        mutableListOf()
    )

    private val stiStopInfo = StopInfo("2020-07-31T14:55:38",
        "Stillorgan",
        "STI",
        "Green Line services operating normally",
        mutableListOf()
    )

    private var successState = ResponseState.Success(marStopInfo)
    private val errorState = ResponseState.Error(Throwable(), 404)

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = ForecastViewModel(repository)
    }

    @After
    fun tearDown() {
        unmockkAll()

        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun `should request trams forecast and finished with success state`() {
        coEvery { repository.tramsForecast(stop = any()) } returns successState

        viewModel.stopInfoState.observeForever(observer)
        viewModel.requestTramsForecast()

        verify(exactly = 1) { observer.onChanged(State.Loading) }
        verify(exactly = 1) { observer.onChanged(State.Success(marStopInfo)) }

        coVerify { repository.tramsForecast(stop = any()) }
    }

    @Test
    fun `should request trams forecast and finished with error state`() {
        coEvery { repository.tramsForecast(stop = any()) } returns errorState

        viewModel.stopInfoState.observeForever(observer)
        viewModel.requestTramsForecast()

        verify(exactly = 1) { observer.onChanged(State.Loading) }
        verify(exactly = 1) { observer.onChanged(State.Error("An unknown error occurred")) }

        coVerify { repository.tramsForecast(stop = any()) }

    }

    @Test
    fun `should request trams forecast from Marlborough at min date time`() {
        successState = ResponseState.Success(marStopInfo)
        coEvery { repository.tramsForecast(stop = LuasStop.MARLBOROUGH.abv) } returns successState
        val date = "2020-08-01T00:00:00".toDate(DatePatterns.yyyy_MM_ddTHH_mm_ss)
        viewModel.stopInfoState.observeForever(observer)
        viewModel.requestTramsForecast(date = date)

        verify(exactly = 1) { observer.onChanged(State.Loading) }
        verify(exactly = 1) { observer.onChanged(State.Success(marStopInfo)) }

        coVerify { repository.tramsForecast(stop = LuasStop.MARLBOROUGH.abv) }
    }

    @Test
    fun `should request trams forecast from Marlborough at max date time`() {
        successState = ResponseState.Success(marStopInfo)
        coEvery { repository.tramsForecast(stop = LuasStop.MARLBOROUGH.abv) } returns successState
        val date = "2020-08-01T12:00:00".toDate(DatePatterns.yyyy_MM_ddTHH_mm_ss)
        viewModel.stopInfoState.observeForever(observer)
        viewModel.requestTramsForecast(date = date)

        verify(exactly = 1) { observer.onChanged(State.Loading) }
        verify(exactly = 1) { observer.onChanged(State.Success(marStopInfo)) }

        coVerify { repository.tramsForecast(stop = LuasStop.MARLBOROUGH.abv) }
    }

    @Test
    fun `should request trams forecast from Marlborough at given date time`() {
        successState = ResponseState.Success(marStopInfo)
        coEvery { repository.tramsForecast(stop = LuasStop.MARLBOROUGH.abv) } returns successState
        val date = "2020-08-01T09:00:00".toDate(DatePatterns.yyyy_MM_ddTHH_mm_ss)
        viewModel.stopInfoState.observeForever(observer)
        viewModel.requestTramsForecast(date = date)

        verify(exactly = 1) { observer.onChanged(State.Loading) }
        verify(exactly = 1) { observer.onChanged(State.Success(marStopInfo)) }

        coVerify { repository.tramsForecast(stop = LuasStop.MARLBOROUGH.abv) }
    }

    @Test
    fun `should request trams forecast from Stillorgan at min date time`() {
        successState = ResponseState.Success(stiStopInfo)
        coEvery { repository.tramsForecast(stop = LuasStop.STILLORGAN.abv) } returns successState
        val date = "2020-08-01T12:01:00".toDate(DatePatterns.yyyy_MM_ddTHH_mm_ss)
        viewModel.stopInfoState.observeForever(observer)
        viewModel.requestTramsForecast(date = date)

        verify(exactly = 1) { observer.onChanged(State.Loading) }
        verify(exactly = 1) { observer.onChanged(State.Success(stiStopInfo)) }

        coVerify { repository.tramsForecast(stop = LuasStop.STILLORGAN.abv) }
    }

    @Test
    fun `should request trams forecast from Stillorgan at max date time`() {
        successState = ResponseState.Success(stiStopInfo)
        coEvery { repository.tramsForecast(stop = LuasStop.STILLORGAN.abv) } returns successState
        val date = "2020-08-01T23:59:00".toDate(DatePatterns.yyyy_MM_ddTHH_mm_ss)
        viewModel.stopInfoState.observeForever(observer)
        viewModel.requestTramsForecast(date = date)

        verify(exactly = 1) { observer.onChanged(State.Loading) }
        verify(exactly = 1) { observer.onChanged(State.Success(stiStopInfo)) }

        coVerify { repository.tramsForecast(stop = LuasStop.STILLORGAN.abv) }
    }

    @Test
    fun `should request trams forecast from Stillorgan at given date time`() {
        successState = ResponseState.Success(stiStopInfo)
        coEvery { repository.tramsForecast(stop = LuasStop.STILLORGAN.abv) } returns successState
        val date = "2020-08-01T16:30:00".toDate(DatePatterns.yyyy_MM_ddTHH_mm_ss)
        viewModel.stopInfoState.observeForever(observer)
        viewModel.requestTramsForecast(date = date)

        verify(exactly = 1) { observer.onChanged(State.Loading) }
        verify(exactly = 1) { observer.onChanged(State.Success(stiStopInfo)) }

        coVerify { repository.tramsForecast(stop = LuasStop.STILLORGAN.abv) }
    }
}