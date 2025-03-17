package com.seiberl.protrackreader.persistance.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.seiberl.protrackreader.persistance.entities.Jump
import com.seiberl.protrackreader.persistance.models.JumpDetail
import com.seiberl.protrackreader.persistance.views.JumpMetaData
import kotlinx.coroutines.flow.Flow

@Dao
interface JumpDao {

    @Insert
    fun insert(jumps: List<Jump>)

    @Insert
    fun insert(jump: Jump)

    @Update
    fun update(jump: Jump)

    @Update
    fun update(jumps: List<Jump>)

    @Delete
    fun delete(jump: Jump): Int

    @Transaction
    @Query("SELECT * FROM Jumps")
    fun observeAll(): Flow<List<Jump>>

    @Transaction
    @Query("SELECT * FROM JumpMetaData")
    fun observeJumpMetaData(): Flow<List<JumpMetaData>>

    @Transaction
    @Query("SELECT Number FROM Jumps")
    fun observeJumpNumbers(): Flow<List<Int>>

    @Transaction
    @Query("SELECT * FROM Jumps WHERE Number IN (:jumpNumbers)")
    fun getFilteredJumps(jumpNumbers: List<Int>): List<Jump>

    @Transaction
    @Query("SELECT * FROM Jumps WHERE Number = :jumpNr")
    fun getJumpByNumber(jumpNr: Int): JumpDetail?

    @Transaction
    @Query("DELETE FROM Jumps WHERE Number = :jumpNr")
    fun deleteByNumber(jumpNr: Int)

    @Transaction
    @Query("SELECT * FROM Jumps WHERE Number IN (:jumpNumbers)")
    fun getJumpsByNumber(jumpNumbers: List<Int>): List<Jump>
}