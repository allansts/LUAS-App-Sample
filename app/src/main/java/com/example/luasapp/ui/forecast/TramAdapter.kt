package com.example.luasapp.ui.forecast

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.luasapp.model.Tram

class TramAdapter(
    private var items: List<Tram> = emptyList()
): RecyclerView.Adapter<TramViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TramViewHolder {
        return TramViewHolder.create(parent)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: TramViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
    }

    fun submit(items: List<Tram>?) {
        this.items = items ?: emptyList()
        notifyDataSetChanged()
    }
}