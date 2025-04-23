package com.example.myapplication.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
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

        // Initial load
        viewModel.loadAllRooms()

        // Filter button
        binding.btnFilterAvailable.setOnClickListener {
            viewModel.loadAvailableRooms()
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
