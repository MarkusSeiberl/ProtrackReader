package com.seiberl.protrackreader.persistance.views

import androidx.room.DatabaseView
import java.time.Instant

@DatabaseView("SELECT Number as number, Timestamp as timestamp, ExitAltitude as exitAltitude, " +
        "DeploymentAltitude as deploymentAltitude, FreefallTime as freefallTime FROM Jump")
data class JumpMetaData(
    val number: Int,
    val timestamp: Instant,
    val exitAltitude: Double,
    val deploymentAltitude: Double,
    val freefallTime: Long
)