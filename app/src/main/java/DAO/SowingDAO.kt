package DAO

import Entities.Sowing
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface SowingDAO {
    @Query("SELECT * FROM sowing")
    fun getAllSowings(): List<Sowing>

    @Query("SELECT * FROM sowing WHERE id = :sowingId")
    fun getSowingById(sowingId: Int): Sowing?

    @Insert
    fun insertSowing(sowing: Sowing)

    @Query("DELETE FROM sowing WHERE id = :sowingId")
    fun deleteSowingById(sowingId: Int)

    @Update
    fun updateSowing(sowing: Sowing)
}