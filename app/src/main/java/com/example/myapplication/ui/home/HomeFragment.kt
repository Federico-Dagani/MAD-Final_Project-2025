package com.example.myapplication.ui.home

import android.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.databinding.FragmentHomeBinding
import androidx.room.Room
import com.example.myapplication.data.AppDatabase
import com.example.myapplication.ui.home.HomeViewModelFactory

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: HomeViewModel
    private lateinit var adapter: RoomAdapter

    private var isFilteringAvailable = false
    private var selectedRoomType: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root = binding.root

        val db = Room.databaseBuilder(
            requireContext().applicationContext,
            AppDatabase::class.java, "school.db"
        ).build()

        val factory = HomeViewModelFactory(db)
        viewModel = ViewModelProvider(this, factory)[HomeViewModel::class.java]

        adapter = RoomAdapter(emptyList())
        binding.roomRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.roomRecyclerView.adapter = adapter

        viewModel.rooms.observe(viewLifecycleOwner) {
            adapter.update(it)
        }

        setupSpinner()

        // Initial load
        viewModel.loadAllRooms()

        viewModel.isFilteringAvailable.observe(viewLifecycleOwner) { isFiltering ->
            binding.btnFilterAvailable.text = if (isFiltering) "Show All Rooms" else "Show Available Rooms"
        }

        binding.btnFilterAvailable.setOnClickListener {
            val current = viewModel.isFilteringAvailable.value ?: false
            viewModel.setFilteringAvailable(!current)
        }


        return root
    }

    private fun setupSpinner() {
        val roomTypes = listOf("All", "Skybox", "Classroom", "Auditorium")
        val spinnerAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, roomTypes)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerRoomType.adapter = spinnerAdapter

        viewModel.selectedRoomType.observe(viewLifecycleOwner) { selectedType ->
            val index = when (selectedType) {
                null -> 0
                "Skybox" -> 1
                "Classroom" -> 2
                "Auditorium" -> 3
                else -> 0
            }
            if (binding.spinnerRoomType.selectedItemPosition != index) {
                binding.spinnerRoomType.setSelection(index)
            }
        }

        binding.spinnerRoomType.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selected = if (position == 0) null else roomTypes[position]
                viewModel.setSelectedRoomType(selected)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        })
    }

    private fun updateRoomList() {
        if (isFilteringAvailable) {
            viewModel.loadAvailableRooms(selectedRoomType)
        } else {
            viewModel.loadAllRooms(selectedRoomType)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
