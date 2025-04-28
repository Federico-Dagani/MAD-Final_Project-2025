package com.example.myapplication.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.databinding.FragmentHomeBinding
import androidx.room.Room
import com.example.myapplication.R
import com.example.myapplication.data.AppDatabase
import com.example.myapplication.ui.home.HomeViewModelFactory

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: HomeViewModel
    private lateinit var adapter: RoomAdapter

    private var showingAvailableOnly = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

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

        // Load everything at start
        viewModel.loadAllRooms()

        return root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_home, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_filter -> {
                toggleFilter()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    // toggle function
    private fun toggleFilter() {
        if (showingAvailableOnly) {
            viewModel.loadAllRooms()
        } else {
            viewModel.loadAvailableRooms()
        }
        showingAvailableOnly = !showingAvailableOnly
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
