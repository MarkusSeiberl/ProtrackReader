package com.seiberl.protrackreader.persistance

import androidx.room.Database
import androidx.room.RoomDatabase
import com.seiberl.protrackreader.persistance.dao.JumpDao
import com.seiberl.protrackreader.persistance.entities.Jump

@Database(entities = [Jump::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract val jumpDao: JumpDao
}