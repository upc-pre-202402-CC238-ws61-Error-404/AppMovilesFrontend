// AppDataBase.kt
package DB

import DAO.ControlDAO
import DAO.ProductDAO
import DAO.SowingDAO
import Entities.Sowing
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import androidx.room.TypeConverters
import Entities.Control
import Entities.Product
import com.example.chaquitaclla_appmovil_android.sowingsManagement.beans.DateConverter

@Database(entities = [Sowing::class, Control::class, Product::class], version = 5, exportSchema = false)
@TypeConverters(DateConverter::class)
abstract class AppDataBase : RoomDatabase() {
    abstract fun sowingDAO(): SowingDAO
    abstract fun controlDAO(): ControlDAO
    abstract fun productDAO(): ProductDAO

    companion object {
        @Volatile
        private var INSTANCE: AppDataBase? = null

        fun getDatabase(context: Context): AppDataBase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDataBase::class.java,
                    "app_database"
                )
                    .fallbackToDestructiveMigration() // Allow destructive migrations
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}