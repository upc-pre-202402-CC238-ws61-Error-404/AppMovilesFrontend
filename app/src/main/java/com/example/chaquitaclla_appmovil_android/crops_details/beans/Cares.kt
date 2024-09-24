package com.example.chaquitaclla_appmovil_android.crops_details.beans

import java.util.Date

data class Cares(
    val id: Int,
    val suggestion: String,
    val date: Date,
    val sowing: Sowing
)