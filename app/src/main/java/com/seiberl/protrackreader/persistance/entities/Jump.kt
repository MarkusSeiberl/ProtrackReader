package com.seiberl.protrackreader.persistance.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.Instant
import java.util.UUID

@Entity
data class Jump(
    @PrimaryKey
    @ColumnInfo(name = "ID")
    val id: String = UUID.randomUUID().toString(),

    @ColumnInfo(name = "Number")
    val number: Int,

    @ColumnInfo(name = "Timestamp")
    val timestamp: Instant,

    @ColumnInfo(name = "ExitAltitude")
    val exitAltitude: Int?,

    @ColumnInfo(name = "DeploymentAltitude")
    val deploymentAltitude: Int?,

    @ColumnInfo(name = "FreefallTime")
    val freefallTime: Int?,
)