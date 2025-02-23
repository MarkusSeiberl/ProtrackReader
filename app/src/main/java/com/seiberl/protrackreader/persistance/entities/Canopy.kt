package com.seiberl.protrackreader.persistance.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity
data class Canopy(
    @PrimaryKey
    @ColumnInfo(name = "ID")
    val id: String = UUID.randomUUID().toString(),

    @ColumnInfo(name = "Name")
    val name: String,

    @ColumnInfo(name = "Manufacturer")
    val manufacturer: String,

    @ColumnInfo(name = "Size")
    val size: Int,

    @ColumnInfo(name = "Favorite")
    val favorite: Boolean,
)