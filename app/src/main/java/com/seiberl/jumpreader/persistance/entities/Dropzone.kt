package com.seiberl.jumpreader.persistance.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity
data class Dropzone(
    @PrimaryKey
    @ColumnInfo(name = "ID")
    val id: String = UUID.randomUUID().toString(),

    @ColumnInfo(name = "Name")
    val name: String,

    @ColumnInfo(name = "Location")
    val location: String,

    @ColumnInfo(name = "ICAO")
    val icao: String,

    @ColumnInfo(name = "Favorite")
    val favorite: Boolean,
)