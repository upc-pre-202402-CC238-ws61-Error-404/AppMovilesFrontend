package com.example.chaquitaclla_appmovil_android.sowingsManagement.beans

import java.util.Date

class Sowing(
    var id: Int,
    var startDate: Date,
    var endDate: Date,
    var areaLand: Int,
    var status: Boolean,
    var phenologicalPhase: Int,
    var cropId: Int,
    var phenologicalPhaseName: String
)