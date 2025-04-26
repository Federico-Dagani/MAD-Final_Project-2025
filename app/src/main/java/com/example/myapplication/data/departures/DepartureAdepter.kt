// ui/DepartureAdapter.kt
package com.example.myapplication.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.data.departures.Departure

class DepartureAdapter(
    private var departures: List<Departure>,
    private val onItemClick: (Departure) -> Unit
) : RecyclerView.Adapter<DepartureAdapter.DepartureViewHolder>() {

    fun updateDepartures(newDepartures: List<Departure>) {
        departures = newDepartures
        notifyDataSetChanged()
    }

    inner class DepartureViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(departure: Departure) {
            itemView.findViewById<TextView>(R.id.time_text).text = departure.time
            itemView.findViewById<TextView>(R.id.direction_text).text = departure.direction
            itemView.findViewById<TextView>(R.id.track_text).text = departure.track ?: "-"
            itemView.setOnClickListener { onItemClick(departure) } //prepped for above mentioned Callback
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DepartureViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.departure_item, parent, false)
        return DepartureViewHolder(view)
    }

    override fun onBindViewHolder(holder: DepartureViewHolder, position: Int) {
        holder.bind(departures[position])
    }

    override fun getItemCount() = departures.size
}