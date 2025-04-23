package com.example.myapplication.ui.map

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
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

        /*
        val textView: TextView = binding.textMap
        mapViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
         */

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

        //set polygons BUILDING A
        mapViewModel.setPolygonForFloor()

        //draw polygons BUILDING A
        drawPolygonForFloor("0")

        //set and draw polygons BUILDING F
        drawBuildingF()

        gMap.setOnIndoorStateChangeListener(object : GoogleMap.OnIndoorStateChangeListener {
            override fun onIndoorBuildingFocused() {
                // Optional - no action needed
            }

            override fun onIndoorLevelActivated(building: IndoorBuilding) {
                val activeLevel = building.activeLevelIndex
                val levelName = building.levels[activeLevel].name
                drawPolygonForFloor(levelName)
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
}