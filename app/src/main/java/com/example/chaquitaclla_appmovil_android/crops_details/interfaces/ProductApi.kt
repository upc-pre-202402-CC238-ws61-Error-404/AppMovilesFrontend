// app/src/main/java/com/example/chaquitaclla_appmovil_android/crops_details/interfaces/ProductApi.kt
package com.example.chaquitaclla_appmovil_android.crops_details.interfaces

import Entities.Product
import retrofit2.http.GET
import retrofit2.http.Path

interface ProductApi {
    @GET("/api/v1/crops-management/crops/{cropId}/products")
    suspend fun getProductsByCropId(@Path("cropId") cropId: Int): List<Product>
}