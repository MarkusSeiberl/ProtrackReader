package com.seiberl.jumpreader.persistance.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.seiberl.jumpreader.persistance.entities.Aircraft
import kotlinx.coroutines.flow.Flow

@Dao
interface AircraftDao {

    @Insert
    fun insert(aircraft: Aircraft)

    @Delete
    fun delete(aircraft: Aircraft)

    @Update
    fun update(aircraft: List<Aircraft>)

    @Query("SELECT * FROM Aircraft")
    fun getAll(): List<Aircraft>

    @Query("SELECT * FROM Aircraft")
    fun observeAll(): Flow<List<Aircraft>>

    @Query("UPDATE Aircraft SET Favorite = :favorite WHERE ID = :id")
    fun star(id: String, favorite: Boolean)

    @Query("SELECT * FROM Aircraft WHERE Favorite = 1")
    fun getFavorites(): List<Aircraft>

    @Query("SELECT * FROM Aircraft WHERE Favorite = 1")
    fun observeFavorite(): Flow<Aircraft>
}