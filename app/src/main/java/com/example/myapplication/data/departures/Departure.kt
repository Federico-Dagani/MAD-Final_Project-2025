package com.example.myapplication.data.departures

//Data class matching the JSON structure of the API response
data class Departure(
    val id: String,
    val time: String,
    val direction: String,
    val track: String?,
    val delay: Int = 0
)