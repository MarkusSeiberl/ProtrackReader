package com.seiberl.protrackreader.persistance.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.seiberl.protrackreader.persistance.entities.Dropzone
import kotlinx.coroutines.flow.Flow

@Dao
interface DropzoneDao {

    @Insert
    fun insert(dropzone: Dropzone)

    @Delete
    fun delete(dropzone: Dropzone)

    @Query("SELECT * FROM Dropzone")
    fun getAll(): List<Dropzone>

    @Query("SELECT * FROM Dropzone")
    fun observeAll(): Flow<List<Dropzone>>
}