package com.example.luasapp.ui.forecast

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.luasapp.R
import com.example.luasapp.extensions.toDate
import com.example.luasapp.extensions.toString
import com.example.luasapp.model.StopInfo
import com.example.luasapp.ui.common.BaseActivity
import com.example.luasapp.utils.DatePatterns
import com.example.luasapp.utils.LuasStop.MARLBOROUGH
import com.example.luasapp.utils.State
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_forecast.bt_update
import kotlinx.android.synthetic.main.activity_forecast.recycler_view
import kotlinx.android.synthetic.main.activity_forecast.swipe_refresh
import kotlinx.android.synthetic.main.activity_forecast.tv_datetime
import kotlinx.android.synthetic.main.activity_forecast.tv_stop_message
import kotlinx.android.synthetic.main.activity_forecast.tv_stop_title
import java.util.Date

@AndroidEntryPoint
class ForecastActivity : BaseActivity() {

    private val viewModel: ForecastViewModel by viewModels()
    private var adapter = TramAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forecast)


        setup()
    }

    private fun setup() {
        setupObserver()

        swipe_refresh.setOnRefreshListener {
            requestForecast()
        }

        bt_update.setOnClickListener {
            requestForecast()
        }

        recycler_view.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        recycler_view.setHasFixedSize(true)
        recycler_view.adapter = adapter

    }

    private fun setupObserver() {
        viewModel.stopInfoState.observe(this, Observer { state ->
            when(state) {
                is State.Loading -> isLoading = true
                is State.Success -> renderSuccessState(state)
                is State.Error -> renderErrorState(state)
            }
        })

        requestForecast()
    }

    private fun requestForecast() {
        if (isConnected) {
            viewModel.requestTramsForecast()
        }
        else {
            isLoading = false
            swipe_refresh.isRefreshing = false
            showNoInternetConnection()
        }
    }

    private fun renderErrorState(state: State.Error) {
        isLoading = false
        swipe_refresh.isRefreshing = false
        Toast.makeText(this, state.message, Toast.LENGTH_SHORT).show()
    }

    private fun renderSuccessState(state: State.Success<StopInfo>) {
        isLoading = false
        swipe_refresh.isRefreshing = false
        updateViews(state.data)
    }

    private fun updateViews(info: StopInfo) {
        tv_stop_title.text = info.stop
        tv_stop_message.text = info.message

        val date = info.created.toDate(DatePatterns.yyyy_MM_ddTHH_mm_ss) ?: Date()

        tv_datetime.text = String.format(
            getString(R.string.last_update),
            date.toString(DatePatterns.dd_MM_yyyy_HH_mm)
        )

        adapter.submit(
            when(info.stopAbv.equals(MARLBOROUGH.abv, true)) {
                true -> info.directions.lastOrNull()?.trams
                else -> info.directions.firstOrNull()?.trams
            }
        )
    }
}