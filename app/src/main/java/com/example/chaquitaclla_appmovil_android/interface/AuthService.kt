package com.example.chaquitaclla_appmovil_android.`interface`

import com.example.chaquitaclla_appmovil_android.model.SignUpRequest
import com.example.chaquitaclla_appmovil_android.model.SignUpResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {
    @POST("/api/v1/authentication/sign-up")
    fun signUp(@Body request: SignUpRequest): Call<SignUpResponse>

    @POST("/api/v1/authentication/sign-in")
    fun signIn(@Body request: SignUpRequest): Call<SignUpResponse>
}