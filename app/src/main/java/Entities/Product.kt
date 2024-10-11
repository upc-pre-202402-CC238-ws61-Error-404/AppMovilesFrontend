package Entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "products")
data class Product(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val sowingId: Int,
    var name: String,
    var type: String,
    var quantity: Float,
    var date: Date
)