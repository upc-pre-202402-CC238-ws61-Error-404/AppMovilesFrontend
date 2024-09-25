package com.example.chaquitaclla_appmovil_android.statistics.beans

/**
 * Represents a sowing.
 * Example:
 * {
 *     "id": 1,
 *     "startDate": "2024-09-24T01:44:59.882595",
 *     "endDate": "2025-03-24T01:44:59.882595",
 *     "areaLand": 100,
 *     "status": false,
 *     "phenologicalPhase": 0,
 *     "cropId": 1,
 *     "phenologicalPhaseName": "Germination"
 *   }
 */
class Sowing(
    var id: Int,
    var startDate: String,
    var endDate: String,
    var areaLand: Int,
    var status: Boolean,
    var phenologicalPhase: Int,
    var cropId: Int,
    var phenologicalPhaseName: String
) {

}