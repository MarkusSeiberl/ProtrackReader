package com.seiberl.protrackreader.persistance.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.seiberl.protrackreader.persistance.entities.Aircraft
import kotlinx.coroutines.flow.Flow

@Dao
interface AircraftDao {

    @Insert
    fun insert(aircraft: Aircraft)

    @Delete
    fun delete(aircraft: Aircraft)

    @Query("SELECT * FROM Aircraft")
    fun getAll(): List<Aircraft>

    @Query("SELECT * FROM Aircraft")
    fun observeAll(): Flow<List<Aircraft>>

    @Query("UPDATE Aircraft SET Favorite = :favorite WHERE ID = :id")
    fun starAircraft(id: String, favorite: Boolean)
}