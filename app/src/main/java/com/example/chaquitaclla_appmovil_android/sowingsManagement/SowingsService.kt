// SowingsService.kt
package com.example.chaquitaclla_appmovil_android.sowingsManagement

import android.util.Log
import com.example.chaquitaclla_appmovil_android.sowingsManagement.beans.SowingDos
import com.example.chaquitaclla_appmovil_android.sowingsManagement.beans.Crop
import com.example.chaquitaclla_appmovil_android.sowingsManagement.interfaces.SowingsApi
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.SocketException
import io.github.cdimascio.dotenv.dotenv

class SowingsService {
    val dotenv = dotenv() {
        directory = "/assets"
        filename = "env"
    }
    private val api: SowingsApi
    private val token = dotenv["BEARER_TOKEN"]

    init {
        Log.d("SowingsService", "Initializing SowingsService")
        val client = OkHttpClient.Builder().addInterceptor { chain ->
            val original = chain.request()
            val requestBuilder: Request.Builder = original.newBuilder()
                .header("Authorization", "Bearer $token")
            val request: Request = requestBuilder.build()
            chain.proceed(request)
        }.build()

        val retrofit = Retrofit.Builder()
            .baseUrl(dotenv["API_URL"])
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        api = retrofit.create(SowingsApi::class.java)
        Log.d("SowingsService", "SowingsService initialized")
    }


    suspend fun getCropById(id: Int): Crop? {
        Log.d("SowingsService", "Fetching crop with ID: $id")
        return try {
            val crop = api.getCropById(id)
            Log.d("SowingsService", "Raw JSON response: $crop")
            crop
        } catch (e: SocketException) {
            Log.e("SowingsService", "SocketException: ${e.message}")
            null
        }
    }

    suspend fun getAllCrops(): List<Crop> {
        Log.d("SowingsService", "Fetching all crops")
        return try {
            val crops = api.getAllCrops()
            Log.d("SowingsService", "Raw JSON response: $crops")
            crops
        } catch (e: SocketException) {
            Log.e("SowingsService", "SocketException: ${e.message}")
            emptyList()
        }
    }

    suspend fun addSowing(sowing: SowingDos) {
        Log.d("SowingsService", "Adding new sowing with payload: ${sowing.toString()}")
        try {
            val response = api.addSowing(sowing)
            Log.d("SowingsService", "Sowing added successfully, response: $response")
        } catch (e: SocketException) {
            Log.e("SowingsService", "SocketException: ${e.message}")
        } catch (e: Exception) {
            Log.e("SowingsService", "Error adding sowing: ${e.message}")
        }
    }
}