// Sowing.kt
package com.example.chaquitaclla_appmovil_android.sowingsManagement.beans

import com.google.gson.annotations.JsonAdapter
import java.util.Date

class Sowing(
    var id: Int,
    @JsonAdapter(DateDeserializer::class)
    var startDate: Date,
    @JsonAdapter(DateDeserializer::class)
    var endDate: Date,
    var areaLand: Double, // Change to Double
    var status: Boolean,
    var phenologicalPhase: Int,
    var cropId: Int,
    var phenologicalPhaseName: String
)
