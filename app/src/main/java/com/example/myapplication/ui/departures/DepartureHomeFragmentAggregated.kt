package com.example.myapplication.ui.departures

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentDepartureBinding

class DepartureHomeFragmentAggregated : Fragment() {

    private var _binding: FragmentDepartureBinding? = null
    private val binding get() = _binding!!

    private val stationList = listOf(
        "DR Byen" to "008663311",
        "Islands Brygge" to "008603310"
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDepartureBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        stationList.getOrNull(0)?.let { (name, id) ->
            childFragmentManager.commit {
                setReorderingAllowed(true) // Improves performance by allowing the system to optimize the order of fragment transactions
                replace(
                    R.id.departure_board_container_one,
                    DepartureBoardFragmentSingle.newInstance(name, id)
                )
            }
        }
        stationList.getOrNull(1)?.let { (name, id) ->
            childFragmentManager.commit {
                setReorderingAllowed(true)
                replace(
                    R.id.departure_board_container_two,
                    DepartureBoardFragmentSingle.newInstance(name, id)
                )
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}