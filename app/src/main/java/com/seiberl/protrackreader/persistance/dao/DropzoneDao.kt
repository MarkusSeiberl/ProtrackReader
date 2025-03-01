package com.seiberl.protrackreader.persistance.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.seiberl.protrackreader.persistance.entities.Aircraft
import com.seiberl.protrackreader.persistance.entities.Dropzone
import kotlinx.coroutines.flow.Flow

@Dao
interface DropzoneDao {

    @Insert
    fun insert(dropzone: Dropzone)

    @Delete
    fun delete(dropzone: Dropzone)

    @Update
    fun update(dropzone: List<Dropzone>)

    @Query("SELECT * FROM Dropzone")
    fun getAll(): List<Dropzone>

    @Query("SELECT * FROM Dropzone")
    fun observeAll(): Flow<List<Dropzone>>

    @Query("UPDATE Dropzone SET Favorite = :favorite WHERE ID = :id")
    fun star(id: String, favorite: Boolean)

    @Query("SELECT * FROM Dropzone WHERE Favorite = 1")
    fun getFavorites(): List<Dropzone>

    @Query("SELECT * FROM Dropzone WHERE Favorite = 1")
    fun observeFavorite(): Flow<Dropzone>
}