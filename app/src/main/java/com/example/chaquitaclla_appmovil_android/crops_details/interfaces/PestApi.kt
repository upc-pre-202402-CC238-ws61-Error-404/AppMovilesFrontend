// app/src/main/java/com/example/chaquitaclla_appmovil_android/crops_details/interfaces/PestApi.kt
package com.example.chaquitaclla_appmovil_android.crops_details.interfaces

import com.example.chaquitaclla_appmovil_android.crops_details.beans.Pest
import retrofit2.http.GET
import retrofit2.http.Path

interface PestApi {
    @GET("/api/v1/crops-management/crops/{cropId}/pests")
    suspend fun getPestsByCropId(@Path("cropId") cropId: Int): List<Pest>
}