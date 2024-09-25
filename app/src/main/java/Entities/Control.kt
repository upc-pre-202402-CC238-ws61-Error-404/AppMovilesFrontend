// Control.kt
package Entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "controls")
data class Control(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val sowingId: Int,
    var sowingCondition: String,
    var stemCondition: String,
    var sowingSoilMoisture: String,
    val date: Date
)