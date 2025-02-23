package com.seiberl.protrackreader.persistance.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.seiberl.protrackreader.persistance.entities.Aircraft
import com.seiberl.protrackreader.persistance.entities.Canopy
import kotlinx.coroutines.flow.Flow

@Dao
interface CanopyDao {

    @Insert
    fun insert(canopy: Canopy)

    @Delete
    fun delete(canopy: Canopy)

    @Update
    fun update(canopy: List<Canopy>)

    @Query("SELECT * FROM Canopy")
    fun getAll(): List<Canopy>

    @Query("SELECT * FROM Canopy")
    fun observeAll(): Flow<List<Canopy>>

    @Query("UPDATE Canopy SET Favorite = :favorite WHERE ID = :id")
    fun star(id: String, favorite: Boolean)

    @Query("SELECT * FROM Canopy WHERE Favorite = 1")
    fun getFavorites(): List<Canopy>
}