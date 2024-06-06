package com.seiberl.protrackreader.persistance

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.seiberl.protrackreader.persistance.converter.Converter
import com.seiberl.protrackreader.persistance.dao.JumpDao
import com.seiberl.protrackreader.persistance.entities.Jump
import com.seiberl.protrackreader.persistance.views.JumpMetaData

@Database(entities = [Jump::class], views = [JumpMetaData::class], version = 1)
@TypeConverters(Converter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract val jumpDao: JumpDao
}