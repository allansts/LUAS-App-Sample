package com.example.luasapp.ui.forecast

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.luasapp.R
import com.example.luasapp.model.Tram
import kotlinx.android.synthetic.main.item_tram.view.ll_due_time
import kotlinx.android.synthetic.main.item_tram.view.tv_due
import kotlinx.android.synthetic.main.item_tram.view.tv_min
import kotlinx.android.synthetic.main.item_tram.view.tv_tram_destination
import kotlinx.android.synthetic.main.item_tram.view.tv_tram_due

class TramViewHolder (var view: View): RecyclerView.ViewHolder(view) {

    companion object {
        fun create(parent: ViewGroup): TramViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_tram, parent, false)
            return TramViewHolder(view)
        }

        const val DUE = "due"
    }

    fun bind(item: Tram) {
        view.ll_due_time.visibility =
            if (item.dueMins.isEmpty()) View.GONE else View.VISIBLE

        settingDue(
            if (item.dueMins.contains(DUE, true)) View.GONE else View.VISIBLE
        )

        view.tv_tram_destination.text = item.destination
        view.tv_tram_due.text = getDue(item.dueMins)
    }

    private fun settingDue(visibility: Int) {
        view.tv_due.visibility = visibility
        view.tv_min.visibility = visibility
    }

    private fun getDue(dueMins: String): String {
        if (dueMins.length == 1) {
            return "0$dueMins"
        }
        return dueMins
    }
}