package com.seiberl.protrackreader.persistance.views

import androidx.room.DatabaseView

@DatabaseView("SELECT Number as number, Timestamp as timestamp, ExitAltitude as exitAltitude, " +
        "DeploymentAltitude as deploymentAltitude, FreefallTime as freefallTime FROM Jump")
data class JumpMetaData(
    val number: Int,
    val timestamp: Long,
    val exitAltitude: Double,
    val deploymentAltitude: Double,
    val freefallTime: Long
)