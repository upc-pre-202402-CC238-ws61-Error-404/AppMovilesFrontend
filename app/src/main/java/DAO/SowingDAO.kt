package DAO

import Entities.Sowing
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface SowingDAO {
    @Query("SELECT * FROM sowing")
    fun getAllSowings(): List<Sowing>

    @Insert
    fun insertSowing(sowing: Sowing)
}