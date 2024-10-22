package com.example.chaquitaclla_appmovil_android.crops_details

import CustomDateTypeAdapter
import android.util.Log
import com.example.chaquitaclla_appmovil_android.SessionManager
import com.example.chaquitaclla_appmovil_android.crops_details.beans.Disease
import com.example.chaquitaclla_appmovil_android.crops_details.interfaces.DiseaseApi
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import io.github.cdimascio.dotenv.dotenv
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.SocketException
import java.util.Date

class DiseaseService {
    val dotenv = dotenv {
        directory = "/assets"
        filename = "env"
    }
    private val api: DiseaseApi
    private val token = SessionManager.token

    init {
        val client = OkHttpClient.Builder().addInterceptor { chain ->
            val original = chain.request()
            val requestBuilder: Request.Builder = original.newBuilder()
                .header("Authorization", "Bearer $token")
            val request: Request = requestBuilder.build()
            chain.proceed(request)
        }.build()

        val gson: Gson = GsonBuilder()
            .registerTypeAdapter(Date::class.java, CustomDateTypeAdapter())
            .create()

        val retrofit = Retrofit.Builder()
            .baseUrl(dotenv["API_URL"])
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        api = retrofit.create(DiseaseApi::class.java)
    }

    suspend fun getDiseaseById(id: Int): Disease? {
        return try {
            api.getDiseaseById(id)
        } catch (e: SocketException) {
            Log.e("DiseaseService", "SocketException: ${e.message}")
            null
        }
    }

    suspend fun getDiseasesByCropId(cropId: Int): List<Disease> {
        return try {
            api.getDiseasesByCropId(cropId)
        } catch (e: SocketException) {
            Log.e("DiseaseService", "SocketException: ${e.message}")
            emptyList()
        }
    }
}