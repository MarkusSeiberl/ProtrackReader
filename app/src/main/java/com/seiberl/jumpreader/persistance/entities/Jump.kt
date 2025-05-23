package com.seiberl.jumpreader.persistance.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.Instant
import java.util.UUID

@Entity(
    tableName = "Jumps",
    foreignKeys = [
        ForeignKey(
            entity = Aircraft::class,
            parentColumns = arrayOf("ID"),
            childColumns = arrayOf("AircraftID"),
            onUpdate = ForeignKey.SET_NULL,
            onDelete = ForeignKey.SET_NULL
        ),
        ForeignKey(
            entity = Canopy::class,
            parentColumns = arrayOf("ID"),
            childColumns = arrayOf("CanopyID"),
            onUpdate = ForeignKey.SET_NULL,
            onDelete = ForeignKey.SET_NULL
        ),
        ForeignKey(
            entity = Dropzone::class,
            parentColumns = arrayOf("ID"),
            childColumns = arrayOf("DropzoneID"),

            onUpdate = ForeignKey.SET_NULL,
            onDelete = ForeignKey.SET_NULL
        )
    ],
    indices = [
        Index(name = "FK_Jump_Aircraft", value = ["AircraftID"]),
        Index(name = "FK_Jump_Canopy", value = ["CanopyID"]),
        Index(name = "FK_Jump_Dropzone", value = ["DropzoneID"])
    ]
)
data class Jump(
    @PrimaryKey
    @ColumnInfo(name = "ID")
    val id: String = UUID.randomUUID().toString(),

    @ColumnInfo(name = "ProtrackID")
    val protrackId: String,

    @ColumnInfo(name = "Number")
    val number: Int,

    @ColumnInfo(name = "Timestamp")
    val timestamp: Instant,

    @ColumnInfo(name = "ExitAltitude")
    val exitAltitude: Int,

    @ColumnInfo(name = "DeploymentAltitude")
    val deploymentAltitude: Int,

    @ColumnInfo(name = "FreefallTime")
    val freefallTime: Int,

    @ColumnInfo(name = "AverageSpeed")
    val averageSpeed: Int,

    @ColumnInfo(name = "MaxSpeed")
    val maxSpeed: Int,

    @ColumnInfo(name = "FirstHalfSpeed")
    val firstHalfSpeed: Int,

    @ColumnInfo(name = "SecondHalfSpeed")
    val secondHalfSpeed: Int,

    @ColumnInfo(name = "GroundLevelPressure")
    val groundLevelPressure: Int,

    @ColumnInfo(name = "FreefallRecorded")
    val freefallRecorded: Boolean,

    @ColumnInfo(name = "CanopyRecorded")
    val canopyRecorded: Boolean,

    @ColumnInfo(name = "SampleSize")
    val sampleSize: Int,

    @ColumnInfo(name = "Samples")
    val samples: IntArray,

    @ColumnInfo(name = "AircraftID")
    val aircraftId: String?,

    @ColumnInfo(name = "CanopyID")
    val canopyId: String?,

    @ColumnInfo(name = "DropzoneID")
    val dropzoneId: String?

) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Jump

        if (id != other.id) return false
        if (protrackId != other.protrackId) return false
        if (number != other.number) return false
        if (timestamp != other.timestamp) return false
        if (exitAltitude != other.exitAltitude) return false
        if (deploymentAltitude != other.deploymentAltitude) return false
        if (freefallTime != other.freefallTime) return false
        if (maxSpeed != other.maxSpeed) return false
        if (firstHalfSpeed != other.firstHalfSpeed) return false
        if (secondHalfSpeed != other.secondHalfSpeed) return false
        if (groundLevelPressure != other.groundLevelPressure) return false
        if (freefallRecorded != other.freefallRecorded) return false
        if (canopyRecorded != other.canopyRecorded) return false
        if (sampleSize != other.sampleSize) return false
        if (!samples.contentEquals(other.samples)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + protrackId.hashCode()
        result = 31 * result + number
        result = 31 * result + timestamp.hashCode()
        result = 31 * result + exitAltitude
        result = 31 * result + deploymentAltitude
        result = 31 * result + freefallTime
        result = 31 * result + maxSpeed
        result = 31 * result + firstHalfSpeed
        result = 31 * result + secondHalfSpeed
        result = 31 * result + groundLevelPressure
        result = 31 * result + freefallRecorded.hashCode()
        result = 31 * result + canopyRecorded.hashCode()
        result = 31 * result + sampleSize
        result = 31 * result + samples.contentHashCode()
        return result
    }
}