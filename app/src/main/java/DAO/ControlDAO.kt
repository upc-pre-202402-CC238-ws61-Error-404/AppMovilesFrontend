package DAO

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import Entities.Control

@Dao
interface ControlDAO {
    @Insert
    fun insertControl(control: Control)

    @Query("SELECT * FROM controls WHERE sowingId = :sowingId")
    fun getControlsBySowingId(sowingId: Int): List<Control>
}