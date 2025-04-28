package com.example.myapplication.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Retrofit

object ApiClient { // Singleton object
    // 2. Base URL from Rejseplanen's developer docs (your initial WADL file showed /api/, but their REST endpoint uses /bin/rest.exe/)
    private const val BASE_URl="https://www.rejseplanen.dk/bin/rest.exe/"

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply {//for debugging
            level = HttpLoggingInterceptor.Level.BODY//logs all requests and responses
        })
        .build()

    val instance : DepartureApiService by lazy {//the actual API caller
        Retrofit.Builder()
            .baseUrl(BASE_URl)
            .addConverterFactory(GsonConverterFactory.create()) //Gson converter for JSON to Kotlin
            .client(okHttpClient)//attached the debugging interceptor
            .build()
            .create(DepartureApiService::class.java) //creates the implementation of the interface
    }
}
