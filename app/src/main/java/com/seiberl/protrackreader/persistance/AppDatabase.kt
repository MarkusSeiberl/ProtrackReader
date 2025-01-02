package com.seiberl.protrackreader.persistance

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.seiberl.protrackreader.persistance.converter.Converter
import com.seiberl.protrackreader.persistance.dao.AircraftDao
import com.seiberl.protrackreader.persistance.dao.CanopyDao
import com.seiberl.protrackreader.persistance.dao.DropzoneDao
import com.seiberl.protrackreader.persistance.dao.JumpDao
import com.seiberl.protrackreader.persistance.entities.Aircraft
import com.seiberl.protrackreader.persistance.entities.Canopy
import com.seiberl.protrackreader.persistance.entities.Dropzone
import com.seiberl.protrackreader.persistance.entities.Jump
import com.seiberl.protrackreader.persistance.views.JumpMetaData

@Database(
    entities = [
        Jump::class,
        Aircraft::class,
        Canopy::class,
        Dropzone::class
    ],
    views = [JumpMetaData::class],
    version = 1
)
@TypeConverters(Converter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract val jumpDao: JumpDao
    abstract val aircraftDao: AircraftDao
    abstract val canopyDao: CanopyDao
    abstract val dropzoneDao: DropzoneDao
}