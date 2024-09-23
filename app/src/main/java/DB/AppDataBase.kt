package DB

import DAO.SowingDAO
import Entities.Sowing
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.chaquitaclla_appmovil_android.sowingsManagement.beans.DateConverter

@Database(entities = [Sowing::class], version = 1)
@TypeConverters(DateConverter::class)
abstract class AppDataBase : RoomDatabase() {
    abstract fun sowingDAO(): SowingDAO

    companion object {
        @Volatile
        private var INSTANCE: AppDataBase? = null

        fun getDatabase(context: Context): AppDataBase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }

            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDataBase::class.java,
                    "chaquitaclla_database_sqlite"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}