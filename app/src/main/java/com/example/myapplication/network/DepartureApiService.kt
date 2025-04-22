package com.example.myapplication.network

import com.example.myapplication.ui.home.DepartureBoardFragment
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface DepartureApiService {
    @GET("departureBoard") // specifies this is a GET request to the endpoint departureBoard
    // results in https://www.rejseplanen.dk/api/departureBoard?...
    suspend fun getDepartures(
        //Adds ?accessId=YOUR_KEY to the URL
        @Query("accessId") accessId: String, //passed in in SharedViewModel
        @Query("id") id: String,
        @Query("format") format: String = "json",
    ): Response<DepartureBoardResponse> //wraps the response in a Response object
}