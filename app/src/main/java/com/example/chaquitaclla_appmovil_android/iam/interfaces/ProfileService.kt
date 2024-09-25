package com.example.chaquitaclla_appmovil_android.iam.interfaces

import com.example.chaquitaclla_appmovil_android.iam.beans.ProfileRequest
import com.example.chaquitaclla_appmovil_android.iam.beans.ProfileResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT

interface ProfileService {
    @POST("profiles")
    fun saveProfile(@Header("Authorization") token: String, @Body request: ProfileRequest): Call<ProfileResponse>

    @GET("profiles")
    fun getAllProfiles(@Header("Authorization") token: String): Call<List<ProfileResponse>>

    @GET("profiles/{profileId}")
    fun getProfileById(@Header("Authorization") token: String, profileId: Int): Call<ProfileResponse>

    @PUT("profiles/{profileId}")
    fun updateProfile(@Header("Authorization") token: String, profileId: Int, @Body request: ProfileRequest): Call<ProfileResponse>
}