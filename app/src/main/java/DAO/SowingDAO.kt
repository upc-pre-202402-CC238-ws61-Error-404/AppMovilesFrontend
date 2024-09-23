package DAO

import Entities.Sowing
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface SowingDAO {
    @Query("SELECT * FROM sowing")
    fun getAllSowings(): List<Sowing>

    @Query("SELECT * FROM sowing WHERE id = :sowingId")
    fun getSowingById(sowingId: Int): Sowing?

    @Insert
    fun insertSowing(sowing: Sowing)
}