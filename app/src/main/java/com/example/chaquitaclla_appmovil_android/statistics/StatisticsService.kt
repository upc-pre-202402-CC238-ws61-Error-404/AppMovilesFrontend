package com.example.chaquitaclla_appmovil_android.statistics

import DB.AppDataBase
import android.content.Context
import android.util.Log
import androidx.room.Room
import com.example.chaquitaclla_appmovil_android.SessionManager
import com.example.chaquitaclla_appmovil_android.statistics.beans.StatisticBar
import com.example.chaquitaclla_appmovil_android.statistics.interfaces.StatisticsApi
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.SocketException
import io.github.cdimascio.dotenv.dotenv
import com.github.mikephil.charting.data.PieEntry
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class StatisticsService(context:Context) {
    val dotenv = dotenv() {
        directory = "/assets"
        filename = "env"
    }
    private val api: StatisticsApi
    private val token = SessionManager.token

    private val db = AppDataBase.getDatabase(context)

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

    /**
     * This method call at the database SQL lite table with the name "controls"
     * return an array with all the controls and the crop referent to the cropId
     * with that cropID we can have the name of the crop
     */
    suspend fun getQuantityOfControlsBySowingIdByDB(): List<PieEntry> {
        return withContext(Dispatchers.IO) {
            val controls = db.controlDAO().getAllControls()
            Log.d("StatisticsService", "Controls from DB: $controls")
            val quantityOfControls = mutableMapOf<Int, Int>()

            for (control in controls) {
                val sowingId = control.sowingId
                quantityOfControls[sowingId] = quantityOfControls.getOrDefault(sowingId, 0) + 1
            }

            val pieEntries = mutableListOf<PieEntry>()
            for ((sowingId, count) in quantityOfControls) {
                val sowing = db.sowingDAO().getSowingById(sowingId)
                if (sowing != null) {
                    val crop = api.getCropName(sowing.cropId)
                    pieEntries.add(PieEntry(count.toFloat(), crop.name))
                }
            }

            pieEntries
        }
    }

    /**
     * This method call at the database SQL lite table with the name "sowings"
     * return an array with all the sowings and the crop referent to the cropId
     * with that cropID we can have the name of the crop
     */
    suspend fun getQuantityOfCropsByDB(): List<StatisticBar> {
        return withContext(Dispatchers.IO) {
            val sowings = db.sowingDAO().getAllSowings()
            val quantityOfCrops = mutableMapOf<Int, Int>()

            // Count the number of sowings for each cropId
            for (sowing in sowings) {
                val cropId = sowing.cropId
                quantityOfCrops[cropId] = quantityOfCrops.getOrDefault(cropId, 0) + 1
            }

            // Call the API to get the crop name for each cropId
            val statisticBars = mutableListOf<StatisticBar>()
            for ((cropId, count) in quantityOfCrops) {
                val crop = api.getCropName(cropId) // API call to get the crop name
                statisticBars.add(StatisticBar(crop.name, count.toFloat()))
            }

            statisticBars
        }
    }


}