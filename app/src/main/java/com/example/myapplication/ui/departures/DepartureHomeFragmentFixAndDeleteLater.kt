package com.example.myapplication.ui.departures

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentDepartureHomeBinding
import com.example.myapplication.ui.departures.DepartureBoardFragment

class DepartureHomeFragmentFixAndDeleteLater : Fragment() {

    private var _binding: FragmentDepartureHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_departure_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Embedding the DepartureBoardFragment
        childFragmentManager.commit {
            setReorderingAllowed(true)
            replace(R.id.departure_board_container, DepartureBoardFragment())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}