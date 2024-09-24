package com.example.chaquitaclla_appmovil_android.statistics

import android.util.Log
import com.example.chaquitaclla_appmovil_android.statistics.beans.StatisticBar
import com.example.chaquitaclla_appmovil_android.statistics.interfaces.StatisticsApi
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.SocketException
import io.github.cdimascio.dotenv.dotenv
import com.github.mikephil.charting.data.PieEntry


class StatisticsService {
    val dotenv = dotenv() {
        directory = "/assets"
        filename = "env"
    }
    private val api: StatisticsApi
    private val token = dotenv["BEARER_TOKEN"]

    init {
        val client = OkHttpClient.Builder().addInterceptor { chain ->
            val original = chain.request()
            val requestBuilder: Request.Builder = original.newBuilder()
                .header("Authorization", "Bearer $token")
            val request: Request = requestBuilder.build()
            chain.proceed(request)
        }.build()

        val retrofit = Retrofit.Builder()
            .baseUrl(dotenv["API_URL"])
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        api = retrofit.create(StatisticsApi::class.java)
    }


    /**
     * This method call api/v1/crops-management/sowings
     * return an array with all the sowings and the crop referent to the cropId
     * only use the cropId to calculate the quantity of crops
     * and call api/v1/crops-management/crops/{cropId} to get the name of the crop
     * This function returns a list of StatisticBar with the quantity of crops
     */
    suspend fun getQuantityOfCrops(): List<StatisticBar> {
        return try {
            val sowings = api.getSowings()
            Log.d("StatisticsService", "Raw JSON response: $sowings")

            val quantityOfCrops = mutableMapOf<String, Int>()

            for(sowing in sowings) {
                val cropId = sowing.cropId
                val crop = api.getCropName(cropId)
                quantityOfCrops[crop.name] = quantityOfCrops.getOrDefault(crop.name, 0) + 1
            }

            val statisticBars = quantityOfCrops.map { StatisticBar(it.key, it.value.toFloat()) }
            Log.d("StatisticsService", "Converted data: $statisticBars")
            statisticBars
        } catch (e: SocketException) {
            Log.e("StatisticsService", "SocketException: ${e.message}")
            emptyList()
        }
    }

    /**
     * This method call api/v1/crops-management/sowings/controls
     * return an array with all the controls and the control referent to the sowingId
     * and calculate the quantity of controls by sowingId
     * This function returns a list of PieEntries with the quantity of controls by sowingId
     */
    suspend fun getQuantityOfControlsBySowingId(): List<PieEntry> {
        return try {
            val sowingsControls = api.getSowingsControls()
            Log.d("StatisticsService", "Raw JSON response: $sowingsControls")

            val quantityOfControls = mutableMapOf<Int, Int>()
            for (control in sowingsControls) {
                val sowingId = control.sowingId
                quantityOfControls[sowingId] = quantityOfControls.getOrDefault(sowingId, 0) + 1
            }

            val pieEntries = quantityOfControls.map { PieEntry(it.value.toFloat(), it.key.toString()) }
            Log.d("StatisticsService", "Converted data: $pieEntries")
            pieEntries
        } catch (e: SocketException) {
            Log.e("StatisticsService", "SocketException: ${e.message}")
            emptyList()
        }
    }


}