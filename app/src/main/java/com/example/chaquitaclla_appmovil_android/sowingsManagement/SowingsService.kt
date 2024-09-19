package com.example.chaquitaclla_appmovil_android.sowingsManagement

import android.util.Log
import com.example.chaquitaclla_appmovil_android.sowingsManagement.beans.Sowing
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

    // This method should call http://localhost:5138/api/v1/crops-management/sowings
    // and return all the sowings
    suspend fun getAllSowings(): List<Sowing> {
        return try {
            val sowings = api.getCrops()
            Log.d("SowingsService", "Raw JSON response: $sowings")
            sowings
        } catch (e: SocketException) {
            Log.e("SowingsService", "SocketException: ${e.message}")
            emptyList()
        }
    }


}