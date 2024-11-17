// SowingsService.kt
package com.example.chaquitaclla_appmovil_android.sowingsManagement

import android.util.Log
import com.example.chaquitaclla_appmovil_android.SessionManager
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
    private val dotenv = dotenv {
        directory = "/assets"
        filename = "env"
    }
    private val api: SowingsApi
    private val token: String? = SessionManager.token

    init {
        if (token.isNullOrEmpty()) {
            throw IllegalArgumentException("Bearer token is missing or empty")
        }

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
    }

    suspend fun getCropById(id: Int): Crop? {
        return try {
            val crop = api.getCropById(id)
            crop
        } catch (e: SocketException) {
            Log.e("SowingsService", "SocketException: ${e.message}")
            null
        }
    }

    suspend fun getAllCrops(): List<Crop> {
        return try {
            val crops = api.getAllCrops()
            crops
        } catch (e: SocketException) {
            Log.e("SowingsService", "SocketException: ${e.message}")
            emptyList()
        }
    }
}