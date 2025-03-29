package com.seiberl.jumpreader.persistance.models

import androidx.room.Embedded
import androidx.room.Relation
import com.seiberl.jumpreader.persistance.entities.Aircraft
import com.seiberl.jumpreader.persistance.entities.Canopy
import com.seiberl.jumpreader.persistance.entities.Dropzone
import com.seiberl.jumpreader.persistance.entities.Jump

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
