package com.seiberl.jumpreader.persistance

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.seiberl.jumpreader.persistance.converter.Converter
import com.seiberl.jumpreader.persistance.dao.AircraftDao
import com.seiberl.jumpreader.persistance.dao.CanopyDao
import com.seiberl.jumpreader.persistance.dao.DropzoneDao
import com.seiberl.jumpreader.persistance.dao.JumpDao
import com.seiberl.jumpreader.persistance.entities.Aircraft
import com.seiberl.jumpreader.persistance.entities.Canopy
import com.seiberl.jumpreader.persistance.entities.Dropzone
import com.seiberl.jumpreader.persistance.entities.Jump
import com.seiberl.jumpreader.persistance.views.JumpMetaData

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