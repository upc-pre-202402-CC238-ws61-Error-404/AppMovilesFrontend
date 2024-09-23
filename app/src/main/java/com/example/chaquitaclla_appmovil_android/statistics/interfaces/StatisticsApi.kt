package com.example.chaquitaclla_appmovil_android.statistics.interfaces;

import com.example.chaquitaclla_appmovil_android.statistics.beans.Control
import com.example.chaquitaclla_appmovil_android.statistics.beans.Crop;

import retrofit2.http.GET;

public interface StatisticsApi {
    @GET("crops-management/crops")
    suspend fun getCrops(): List<Crop>

    @GET("crops-management/sowings/controls")
    suspend fun getSowingsControls(): List<Control>
}
