// app/src/main/java/com/example/chaquitaclla_appmovil_android/crops_details/interfaces/ControlApi.kt
package com.example.chaquitaclla_appmovil_android.crops_details.interfaces

import com.example.chaquitaclla_appmovil_android.crops_details.beans.Controls
import retrofit2.http.*

interface ControlApi {
    @GET("/api/v1/crops-management/sowings/{sowingId}/controls")
    suspend fun getControlsBySowingId(@Path("sowingId") sowingId: Int): List<Controls>

    @GET("/api/v1/crops-management/sowings/{sowingId}/controls/{controlId}")
    suspend fun getControlById(@Path("sowingId") sowingId: Int, @Path("controlId") controlId: Int): Controls

    @DELETE("/api/v1/crops-management/sowings/{sowingId}/controls/{controlId}")
    suspend fun deleteControl(@Path("sowingId") sowingId: Int, @Path("controlId") controlId: Int)

    @GET("/api/v1/crops-management/sowings/controls")
    suspend fun getAllControls(): List<Controls>

    @PUT("/api/v1/crops-management/sowings/{sowingId}/controls/{controlId}")
    suspend fun updateControl(@Path("sowingId") sowingId: Int, @Path("controlId") controlId: Int, @Body control: Controls)

    @POST("/api/v1/crops-management/sowings/{sowingId}/controls")
    suspend fun createControl(@Path("sowingId") sowingId: Int, @Body control: Controls): Controls
}