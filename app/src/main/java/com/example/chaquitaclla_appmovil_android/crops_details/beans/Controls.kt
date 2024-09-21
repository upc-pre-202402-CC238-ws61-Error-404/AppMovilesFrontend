package com.example.chaquitaclla_appmovil_android.crops_details.beans

data class Controls(
    val id: Int,
    val sowingId: Int,
    val sowing: Sowing,
    val condition: Boolean,
    val soilMoisture: Boolean,
    val stemCondition: Boolean
)