package com.example.chaquitaclla_appmovil_android.forum.services

import android.util.Log
import com.example.chaquitaclla_appmovil_android.SessionManager
import com.example.chaquitaclla_appmovil_android.forum.interfaces.ForumApi
import com.example.chaquitaclla_appmovil_android.iam.beans.ProfileResponse
import io.github.cdimascio.dotenv.dotenv
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ProfileServiceForum {
    val dotenv = dotenv(){
        directory = "/assets"
        filename = "env"
    }

    private val api: ForumApi
    private val token = SessionManager.token

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

        api = retrofit.create(ForumApi::class.java)
    }

    suspend fun getProfileById(profileId:Int):ProfileResponse? {
        return try {
            val profile = api.getProfileById(profileId)
            Log.d("ProfilesService","Raw JSON response: $profile")
            profile
        }catch (e: Exception) {
            Log.e("ProfilesService", "Error: ${e.message}")
            null
        }
    }

    suspend fun getAllProfiles():List<ProfileResponse>? {
        return try {
            val profiles = api.getAllProfiles()
            Log.d("ProfilesService","Raw JSON response: $profiles")
            profiles
        }catch (e: Exception) {
            Log.e("ProfilesService", "Error: ${e.message}")
            null
        }
    }
}