package com.example.chaquitaclla_appmovil_android.statistics.interfaces;

import com.example.chaquitaclla_appmovil_android.statistics.beans.Control
import com.example.chaquitaclla_appmovil_android.statistics.beans.Crop;
import com.example.chaquitaclla_appmovil_android.statistics.beans.Sowing

import retrofit2.http.GET;
import retrofit2.http.Path

public interface StatisticsApi {

    @GET("crops-management/sowings/controls")
    suspend fun getSowingsControls(): List<Control>

    //Function used to get the name of the crop by the cropId
    @GET("crops-management/crops/{cropId}")
    suspend fun getCropName(@Path("cropId") cropId: Int): Crop

    //Function used to get all sowings
    @GET("crops-management/sowings")
    suspend fun getSowings(): List<Sowing>
}
