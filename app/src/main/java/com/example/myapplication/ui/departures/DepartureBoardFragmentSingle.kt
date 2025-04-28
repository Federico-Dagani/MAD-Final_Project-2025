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

class DepartureBoardFragmentSingle : Fragment(R.layout.frame_departure_board) {

//    private val sharedViewModel: SharedViewModel by viewModels(ownerProducer = { requireParentFragment() })
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private lateinit var stationName: String
    private lateinit var stationId: String

    companion object {//will be used to pass arguments to the fragment (proper way how to create fragments with arguments)
        private const val ARG_STATION_NAME = "station_name"
        private const val ARG_STATION_ID = "station_id"

        fun newInstance(stationName: String, stationId: String): DepartureBoardFragmentSingle {
            return DepartureBoardFragmentSingle().apply {
                arguments = Bundle().apply {
                    putString(ARG_STATION_NAME, stationName)
                    putString(ARG_STATION_ID, stationId)
                }
            }
        }
    }
//    private val stationIds = mapOf(
//        "DR Byen" to "008663311",
//        "Island Brygge" to "008603310",
//    )
//    private val STATION_ID = "008663311" // DR BYEN

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        stationName = arguments?.getString(ARG_STATION_NAME) ?: run {
            showError("Station name not provided")
            return
        }

        stationId = arguments?.getString(ARG_STATION_ID) ?: run {
            showError("Station ID not provided")
            return
        }
        // Set up the title of the Station
        view.findViewById<TextView>(R.id.station_name).text = stationName

        //Sets up the SwipeRefreshLayout -> recyclerView is part of the layout
        val swipeRefresh: SwipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout)
        swipeRefresh.setOnRefreshListener { refreshData()  }

        sharedViewModel.loadSingleStationDepartures(stationId)
        observeDepartures(swipeRefresh)
        observeError(swipeRefresh)
    }

    private fun refreshData() {
        sharedViewModel.loadSingleStationDepartures(stationId) // Replace with actual station ID
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

private fun updateDepartureList(departures: List<Departure>?) {
    if (departures != null) {
        println("DEBUG - updateDepartureList called with ${departures.size} items")
        departures.forEachIndexed { i, d ->
            println("Item $i: ${d.time} | ${d.direction} | Track: ${d.track}")
        }
    }
    when {
        departures == null -> {
            // Show loading indicator
            view?.findViewById<TextView>(R.id.empty_state_text)?.visibility = View.GONE
        }
        departures.isEmpty() -> {
            // Show empty state
            view?.findViewById<TextView>(R.id.empty_state_text)?.visibility = View.VISIBLE
        }
        else -> {
            // Show actual data
            val recyclerView = view?.findViewById<RecyclerView>(R.id.departures_recycler_view)
            recyclerView?.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = DepartureAdapter(departures) { departure ->
                    Toast.makeText(context, "Selected ${departure.direction}", Toast.LENGTH_SHORT).show()
                }
            }
            view?.findViewById<TextView>(R.id.empty_state_text)?.visibility = View.GONE
        }
    }

}

    private fun showError(error: String) {
        Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
    }
}