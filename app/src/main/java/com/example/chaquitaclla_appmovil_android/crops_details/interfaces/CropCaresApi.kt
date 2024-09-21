// app/src/main/java/com/example/chaquitaclla_appmovil_android/crops_details/interfaces/CropCaresApi.kt
package com.example.chaquitaclla_appmovil_android.crops_details.interfaces

import com.example.chaquitaclla_appmovil_android.crops_details.beans.Cares
import retrofit2.http.GET
import retrofit2.http.Path

interface CropCaresApi {
    @GET("crops-management/crops/cares/{id}")
    suspend fun getCareById(@Path("id") id: Int): Cares

    @GET("crops-management/crops/{cropId}/cares")
    suspend fun getCaresByCropId(@Path("cropId") cropId: Int): List<Cares>
}