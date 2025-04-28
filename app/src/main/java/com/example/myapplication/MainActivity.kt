package com.example.myapplication

import SharedViewModel
import android.os.Bundle
import android.view.Window
import androidx.activity.viewModels
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.myapplication.databinding.ActivityMainBinding
import androidx.room.Room
import com.example.myapplication.data.AppDatabase
import com.example.myapplication.data.loadMockData
import com.google.android.material.appbar.MaterialToolbar

class MainActivity : AppCompatActivity() {
    val sharedViewMode: SharedViewModel by viewModels()

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "school.db"
        ).build()

        loadMockData(this, db)

        val navView: BottomNavigationView = binding.navigationBottom

        val navController = findNavController(R.id.navigation_main_host_fragment)
        // Passing each menu ID as a set of Ids because each menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_departures, R.id.navigation_map
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)


    }
}


