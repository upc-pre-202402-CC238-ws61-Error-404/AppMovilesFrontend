package DAO

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import Entities.Control
import androidx.room.Update

@Dao
interface ControlDAO {
    @Insert
    fun insertControl(control: Control)

    @Query("SELECT * FROM controls WHERE sowingId = :sowingId")
    fun getControlsBySowingId(sowingId: Int): List<Control>

    @Query("DELETE FROM controls WHERE id = :controlId")
    fun deleteControl(controlId: Int)

    @Update
    fun updateControl(control: Control)
}