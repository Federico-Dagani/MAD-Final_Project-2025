package com.example.myapplication.ui.departures

import SharedViewModel
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.myapplication.R
import com.example.myapplication.data.departures.Departure
import com.example.myapplication.ui.DepartureAdapter
import kotlinx.coroutines.launch

class DepartureBoardFragment : Fragment(R.layout.frame_departure_board) {

    private val sharedViewModel: SharedViewModel by activityViewModels()
    private val stationIds = mapOf(
        "DR Byen" to "008663311",
        "Island Brygge" to "008603310",
    )
    private val STATION_ID = "008663311" // DR BYEN

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Sets up the SwipeRefreshLayout -> recyclerView is part of the layout
        val swipeRefresh: SwipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout)
        swipeRefresh.setOnRefreshListener {
            refreshData()
        }
        sharedViewModel.loadSingleStationDepartures(STATION_ID)
        observeDepartures(swipeRefresh)
        observeError(swipeRefresh)
    }

    private fun refreshData() {
        sharedViewModel.loadSingleStationDepartures(STATION_ID) // Replace with actual station ID
    }

    private fun observeDepartures(swipeRefresh: SwipeRefreshLayout) {
        lifecycleScope.launch {
            sharedViewModel.departures.collect { departures ->
                updateDepartureList(departures)
                swipeRefresh.isRefreshing = false
            }
        }
    }

    private fun observeError(swipeRefresh: SwipeRefreshLayout) {
        lifecycleScope.launch {
            sharedViewModel.errorMessage.collect { error:String? ->
                if (!error.isNullOrEmpty()) {
                    showError(error)
                    swipeRefresh.isRefreshing = false// STOPS THE SPINNER ON ERROR
                }
            }
        }
    }

    private fun updateDepartureList(departures: List<Departure>) {
        val recyclerView = view?.findViewById<RecyclerView>(R.id.departures_recycler_view)

        val adapter = DepartureAdapter(departures) //possibly useless function
        { departure ->
            Toast.makeText(context, "Selected ${departure.direction}", Toast.LENGTH_SHORT).show()
        }
        recyclerView?.apply {
            layoutManager = LinearLayoutManager(context)
            this.adapter = adapter
        }

        if (departures.isEmpty()) {
            view?.findViewById<TextView>(R.id.empty_state_text)?.visibility = View.VISIBLE
        } else {
            view?.findViewById<TextView>(R.id.empty_state_text)?.visibility = View.GONE
        }
    }

    private fun showError(error: String) {
        Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
    }
}