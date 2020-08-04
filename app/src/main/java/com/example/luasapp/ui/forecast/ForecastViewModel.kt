package com.example.luasapp.ui.forecast

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.luasapp.api.utils.ResponseState
import com.example.luasapp.extensions.toDate
import com.example.luasapp.extensions.toString
import com.example.luasapp.model.Direction
import com.example.luasapp.model.StopInfo
import com.example.luasapp.repository.ForecastRepository
import com.example.luasapp.utils.DatePatterns
import com.example.luasapp.utils.LuasStop
import com.example.luasapp.utils.LuasStop.MARLBOROUGH
import com.example.luasapp.utils.LuasStop.STILLORGAN
import com.example.luasapp.utils.State
import kotlinx.coroutines.launch
import java.util.Date

class ForecastViewModel @ViewModelInject constructor(
    private val repository: ForecastRepository
): ViewModel() {

    companion object {
        const val INBOUND = "inbound"
        const val OUTBOUND = "outbound"
    }

    private val _stopInfoState = MutableLiveData<State<StopInfo>>()
    val stopInfoState: LiveData<State<StopInfo>>
        get() = _stopInfoState

    fun requestTramsForecast(date: Date? = null) = viewModelScope.launch {
        _stopInfoState.postValue(State.Loading)
        val stop = luasStopFrom(date = date ?: Date())
        when (val state = repository.tramsForecast(stop = stop.abv)) {
            is ResponseState.Success -> {
                setupData(stop, state.data)
                _stopInfoState.postValue(
                    State.Success(state.data)
                )
            }
            is ResponseState.Error -> {
                _stopInfoState.postValue(
                    when (state.code) {
                        404 -> State.Error(state.error?.localizedMessage ?: "An unknown error occurred")
                        504 -> State.Error(state.error?.localizedMessage ?: "An unknown error occurred")
                        else -> State.Error("An unknown error occurred")
                    }
                )
            }
        }
    }

    private fun setupData(stop: LuasStop, data: StopInfo) {
        when (stop) {
            MARLBOROUGH -> removeDirection(INBOUND, data.directions)
            else -> removeDirection(OUTBOUND, data.directions)
        }
    }

    private fun removeDirection(bound: String, items: MutableList<Direction>) {
        items.indexOfFirst { it.name.equals(bound, true) }.also {
            if (it != -1) items.removeAt(it)
        }
    }

    private fun luasStopFrom(date: Date) : LuasStop {
        if (isWithinTimeRange(date = date)) return MARLBOROUGH
        return STILLORGAN
    }

    private fun isWithinTimeRange(stop: LuasStop = MARLBOROUGH, date: Date): Boolean {
        val dateStr = date.toString(DatePatterns.dd_MM_yyyy)
        val starTime = "$dateStr ${stop.startTime}".toDate(DatePatterns.dd_MM_yyyy_HH_mm)
        val endTime = "$dateStr ${stop.endTime}".toDate(DatePatterns.dd_MM_yyyy_HH_mm)

        return when {
            starTime == date || endTime == date -> true
            date.after(starTime) && date.before(endTime) -> true
            else -> false
        }
    }

}