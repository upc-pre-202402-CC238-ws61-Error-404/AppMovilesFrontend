package com.example.chaquitaclla_appmovil_android.sowingsManagement.interfaces

import com.example.chaquitaclla_appmovil_android.sowingsManagement.beans.Sowing
import com.example.chaquitaclla_appmovil_android.statistics.beans.Control
import com.example.chaquitaclla_appmovil_android.statistics.beans.Crop
import retrofit2.http.GET

interface SowingsApi {
    @GET("crops-management/sowings")
    suspend fun getCrops(): List<Sowing>

    @GET("crops-management/sowings/controls")
    suspend fun getSowingsControls(): List<Control>
}