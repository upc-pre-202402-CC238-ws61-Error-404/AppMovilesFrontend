// Control.kt
package Entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "controls")
data class Control(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val sowingId: Int,
    val sowingCondition: String,
    val stemCondition: String,
    val sowingSoilMoisture: String
)