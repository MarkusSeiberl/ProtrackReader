package com.seiberl.protrackreader.persistance.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.seiberl.protrackreader.persistance.entities.Aircraft
import com.seiberl.protrackreader.persistance.entities.Canopy
import kotlinx.coroutines.flow.Flow

@Dao
interface CanopyDao {

    @Insert
    fun insert(canopy: Canopy)

    @Delete
    fun delete(canopy: Aircraft)

    @Query("SELECT * FROM Canopy")
    fun getAll(): List<Canopy>

    @Query("SELECT * FROM Canopy")
    fun observeAll(): Flow<List<Canopy>>
}