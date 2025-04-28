package com.example.myapplication.ui.map

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentMapBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.IndoorBuilding
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolygonOptions

import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar

class MapFragment : Fragment(), OnMapReadyCallback {

    private var _binding: FragmentMapBinding? = null

    private lateinit var gMap: GoogleMap
    private lateinit var mapViewModel: MapViewModel

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        mapViewModel = ViewModelProvider(this).get(MapViewModel::class.java)

        _binding = FragmentMapBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // childFragmentManager is used to manage fragments nested inside a Fragment (like SupportMapFragment)
        val mapFragment = childFragmentManager.findFragmentById(R.id.id_map) as? SupportMapFragment
        mapFragment?.getMapAsync(this)

        val searchView = root.findViewById<SearchView>(R.id.room_search_view)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                pointToRoom(query)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                // Optional: do live search if you want
                return false
            }
        })

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onMapReady(googleMap: GoogleMap) {

        gMap = googleMap
        // Set map to normal 2D type
        gMap.mapType = GoogleMap.MAP_TYPE_NORMAL
        // Disable 3D buildings
        gMap.isBuildingsEnabled = false

        val itu_location = LatLng(55.6593701391966, 12.59016680337294)

        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(itu_location, 17.5F))

        mapViewModel.initializeRooms()

        //set polygons BUILDING A
        mapViewModel.setPolygonForFloor()

        //draw polygons BUILDING A
        drawPolygonForFloor("0")

        //set and draw polygons BUILDING F
        drawBuildingF()

        gMap.setOnIndoorStateChangeListener(object : GoogleMap.OnIndoorStateChangeListener {
            override fun onIndoorBuildingFocused() {
                // Optional - no action needed
                checkFloor()
            }

            override fun onIndoorLevelActivated(building: IndoorBuilding) {
                val activeLevel = building.activeLevelIndex
                val levelName = building.levels[activeLevel].name
                drawPolygonForFloor(levelName)
                checkFloor()
            }
        })

    }

    private fun drawBuildingF() {
        gMap.addPolygon(
            PolygonOptions()
            .add(
                LatLng(55.65900012105917, 12.588900794060631),
                LatLng(55.65890377234357, 12.589745049802172),
                LatLng(55.65844682002721, 12.589580298267784),
                LatLng(55.65853707982173, 12.588735838681457)
            )
            .strokeColor(Color.BLACK)
            .fillColor(Color.argb(60, 0, 0, 255))
        )
    }


    private fun drawPolygonForFloor(floorName: String) {
        mapViewModel.currentPolygon?.remove()
        mapViewModel.floorPolygons[floorName]?.let {
            mapViewModel.currentPolygon = gMap.addPolygon(it)
        }
    }

    private var switchFloorSnackbar: Snackbar? = null
    private var pendingRoomFloor: String? = null

    private fun pointToRoom(roomCode: String) {

        val room = mapViewModel.getRoom(roomCode)

        if (room == null) {
            Toast.makeText(requireContext(), "Room not found", Toast.LENGTH_SHORT).show()
            return
        }

        val building = gMap.focusedBuilding
        val activeFloorName = building?.levels?.getOrNull(building.activeLevelIndex)?.name

        if (activeFloorName != room.floor && room.code[1] != 'F') {
            showSwitchFloorSnackbar(room.floor)
        } else {
            dismissSwitchFloorSnackbar()
        }

        // Center camera to the room
        gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(room.location, 20f))

        // Add a marker
        gMap.addMarker(
            MarkerOptions()
                .position(room.location)
                .title(room.name)
                .icon(BitmapDescriptorFactory.defaultMarker())
        )
    }

    private fun showSwitchFloorSnackbar(targetFloor: String) {
        dismissSwitchFloorSnackbar() // Dismiss any existing one first

        pendingRoomFloor = targetFloor

        switchFloorSnackbar = Snackbar.make(requireView(), "Your room is at floor $targetFloor, please select it", Snackbar.LENGTH_INDEFINITE).setTextColor(Color.RED)
        switchFloorSnackbar?.show()
    }

    private fun dismissSwitchFloorSnackbar() {
        switchFloorSnackbar?.dismiss()
        switchFloorSnackbar = null
        pendingRoomFloor = null
    }

    private fun checkFloor() {
        val building = gMap.focusedBuilding
        val activeFloorName = building?.levels?.getOrNull(building.activeLevelIndex)?.name

        if (pendingRoomFloor != null && activeFloorName == pendingRoomFloor) {
            dismissSwitchFloorSnackbar()
        }
    }

}