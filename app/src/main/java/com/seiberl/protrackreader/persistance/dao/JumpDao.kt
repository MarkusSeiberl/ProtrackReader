package com.seiberl.protrackreader.persistance.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.seiberl.protrackreader.persistance.entities.Jump
import kotlinx.coroutines.flow.Flow

@Dao
interface JumpDao {

    @Update
    fun update(jump: Jump): Int

    @Delete
    fun delete(jump: Jump): Int

    @Transaction
    @Query("SELECT * FROM Jump")
    fun observeAll(): Flow<List<Jump>>

    @Transaction
    @Query("SELECT Number FROM Jump")
    fun observeJumpNumbers(): Flow<List<Int>>

}