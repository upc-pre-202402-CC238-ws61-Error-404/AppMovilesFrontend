package com.example.chaquitaclla_appmovil_android.iam

import com.example.chaquitaclla_appmovil_android.iam.`interface`.AuthService
import com.example.chaquitaclla_appmovil_android.iam.interfaces.ProfileService
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import io.github.cdimascio.dotenv.dotenv
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    val dotenv = dotenv() {
        directory = "/assets"
        filename = "env"
    }
    private val BASE_URL = dotenv["API_URL"]
    private val gson: Gson = GsonBuilder().create()
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()
    val authService: AuthService = retrofit.create(AuthService::class.java)
    val profileService: ProfileService = retrofit.create(ProfileService::class.java)
}