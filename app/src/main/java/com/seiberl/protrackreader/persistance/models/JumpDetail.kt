package com.seiberl.protrackreader.persistance.models

import androidx.room.Embedded
import androidx.room.Relation
import com.seiberl.protrackreader.persistance.entities.Aircraft
import com.seiberl.protrackreader.persistance.entities.Canopy
import com.seiberl.protrackreader.persistance.entities.Dropzone
import com.seiberl.protrackreader.persistance.entities.Jump

class JumpDetail(
    @Embedded
    val jump: Jump,
    @Relation(parentColumn = "AircraftID", entityColumn = "ID")
    val aircraft: Aircraft?,
    @Relation(parentColumn = "CanopyID", entityColumn = "ID")
    val canopy: Canopy?,
    @Relation(parentColumn = "DropzoneID", entityColumn = "ID")
    val dropzone: Dropzone?
)
