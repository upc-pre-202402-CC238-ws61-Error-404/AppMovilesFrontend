// ApiClient.kt
package com.example.chaquitaclla_appmovil_android.crops_details

import CustomDateTypeAdapter
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.Date

object ApiClient {
    private val gson: Gson = GsonBuilder()
        .registerTypeAdapter(Date::class.java, CustomDateTypeAdapter())
        .create()

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://your.api.url/")
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    fun <S> createService(serviceClass: Class<S>): S {
        return retrofit.create(serviceClass)
    }
}