package com.example.chaquitaclla_appmovil_android.iam.`interface`

import com.example.chaquitaclla_appmovil_android.iam.beans.SignInRequest
import com.example.chaquitaclla_appmovil_android.iam.beans.SignInResponse
import com.example.chaquitaclla_appmovil_android.iam.beans.SignUpRequest
import com.example.chaquitaclla_appmovil_android.iam.beans.SignUpResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {
    @POST("authentication/sign-up")
    fun signUp(@Body request: SignUpRequest): Call<SignUpResponse>

    @POST("authentication/sign-in")
    fun signIn(@Body request: SignInRequest): Call<SignInResponse>

}