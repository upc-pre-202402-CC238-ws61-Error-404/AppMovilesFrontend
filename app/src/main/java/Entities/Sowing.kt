// Entities/Sowing.kt
package Entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.chaquitaclla_appmovil_android.sowingsManagement.beans.DateDeserializer
import com.google.gson.annotations.JsonAdapter
import java.util.Date

@Entity(tableName = "sowing")
data class Sowing(
    @PrimaryKey(autoGenerate = true)
    val id: Int,

    @ColumnInfo(name = "start_date")
    @JsonAdapter(DateDeserializer::class)
    val startDate: Date,

    @ColumnInfo(name = "end_date")
    @JsonAdapter(DateDeserializer::class)
    val endDate: Date,

    @ColumnInfo(name = "area_land")
    val areaLand: Int,

    @ColumnInfo(name = "status")
    val status: Boolean,

    @ColumnInfo(name = "phenological_phase")
    val phenologicalPhase: Int,

    @ColumnInfo(name = "crop_id")
    val cropId: Int,

    @ColumnInfo(name = "phenological_phase_name")
    val phenologicalPhaseName: String,

    @ColumnInfo(name = "favourite")
    val favourite: Boolean
)