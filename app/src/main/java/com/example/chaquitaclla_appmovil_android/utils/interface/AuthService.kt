package com.example.chaquitaclla_appmovil_android.utils.`interface`

import com.example.chaquitaclla_appmovil_android.utils.model.UserRequest
import com.example.chaquitaclla_appmovil_android.utils.model.UserRequestProfile
import com.example.chaquitaclla_appmovil_android.utils.model.UserResponse
import com.example.chaquitaclla_appmovil_android.utils.model.UserResponseProfile
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthService {
    @POST("api/v1/authentication/sign-up")
    suspend fun signUp(@Body request: UserRequest): Response<UserResponse>

    @POST("api/v1/authentication/sign-in")
    suspend fun signIn(@Body request: UserRequest): Response<UserResponse>

    @POST("api/v1/profiles")
    suspend fun saveProfile(@Body request: UserRequestProfile,
                            @Header("Authorization") token: String): Response<UserResponseProfile>
}