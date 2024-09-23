package com.example.chaquitaclla_appmovil_android.utils

import com.example.chaquitaclla_appmovil_android.utils.`interface`.AuthService
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "https://appchaquitaclla.azurewebsites.net/"
    private val gson: Gson = GsonBuilder().create()
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()
    val authService: AuthService = retrofit.create(AuthService::class.java)
}