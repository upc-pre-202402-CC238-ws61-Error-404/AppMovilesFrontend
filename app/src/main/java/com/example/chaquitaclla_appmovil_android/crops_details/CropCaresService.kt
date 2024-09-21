package com.example.chaquitaclla_appmovil_android.crops_details

import android.util.Log
import com.example.chaquitaclla_appmovil_android.crops_details.beans.Cares
import com.example.chaquitaclla_appmovil_android.crops_details.interfaces.CropCaresApi
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.SocketException
import io.github.cdimascio.dotenv.dotenv

class CropCaresService {
    val dotenv = dotenv {
        directory = "/assets"
        filename = "env"
    }
    private val api: CropCaresApi
    private val token = dotenv["BEARER_TOKEN"]

    init {
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

        api = retrofit.create(CropCaresApi::class.java)
    }

    suspend fun getCareById(id: Int): Cares? {
        return try {
            api.getCareById(id)
        } catch (e: SocketException) {
            Log.e("CropCaresService", "SocketException: ${e.message}")
            null
        }
    }

    suspend fun getCaresByCropId(cropId: Int): List<Cares> {
        return try {
            api.getCaresByCropId(cropId)
        } catch (e: SocketException) {
            Log.e("CropCaresService", "SocketException: ${e.message}")
            emptyList()
        }
    }
}