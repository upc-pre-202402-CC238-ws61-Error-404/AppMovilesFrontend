// app/src/main/java/com/example/chaquitaclla_appmovil_android/crops_details/ControlService.kt
package com.example.chaquitaclla_appmovil_android.crops_details

import com.example.chaquitaclla_appmovil_android.crops_details.interfaces.ControlApi
import com.example.chaquitaclla_appmovil_android.crops_details.beans.Controls
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ControlService {
    private val api: ControlApi

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://your-api-base-url.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        api = retrofit.create(ControlApi::class.java)
    }

    suspend fun getControlsBySowingId(sowingId: Int): List<Controls> {
        return api.getControlsBySowingId(sowingId)
    }

    suspend fun getControlById(sowingId: Int, controlId: Int): Controls {
        return api.getControlById(sowingId, controlId)
    }

    suspend fun deleteControl(sowingId: Int, controlId: Int) {
        api.deleteControl(sowingId, controlId)
    }

    suspend fun getAllControls(): List<Controls> {
        return api.getAllControls()
    }

    suspend fun updateControl(sowingId: Int, controlId: Int, control: Controls) {
        api.updateControl(sowingId, controlId, control)
    }

    suspend fun createControl(sowingId: Int, control: Controls): Controls {
        return api.createControl(sowingId, control)
    }
}