// app/src/main/java/com/example/chaquitaclla_appmovil_android/crops_details/PestService.kt
package com.example.chaquitaclla_appmovil_android.crops_details

import android.content.Context
import android.util.Log
import com.example.chaquitaclla_appmovil_android.EnvUtils
import com.example.chaquitaclla_appmovil_android.crops_details.beans.Pest
import com.example.chaquitaclla_appmovil_android.crops_details.interfaces.PestApi
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.SocketException

class PestService(context: Context) {
    private val env = EnvUtils.loadEnv(context, "env")
    private val apiUrl = env["API_URL"] ?: throw IllegalStateException("API_URL not found in env")
    private val bearerToken = env["BEARER_TOKEN"] ?: throw IllegalStateException("BEARER_TOKEN not found in env")

    private val client = OkHttpClient.Builder().addInterceptor { chain ->
        val original = chain.request()
        val requestBuilder: Request.Builder = original.newBuilder()
            .header("Authorization", "Bearer $bearerToken")
        val request: Request = requestBuilder.build()
        chain.proceed(request)
    }.build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(apiUrl)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val api: PestApi = retrofit.create(PestApi::class.java)

    suspend fun getPestsByCropId(cropId: Int): List<Pest> {
        return try {
            api.getPestsByCropId(cropId)
        } catch (e: SocketException) {
            Log.e("PestService", "SocketException: ${e.message}")
            emptyList()
        }
    }
}