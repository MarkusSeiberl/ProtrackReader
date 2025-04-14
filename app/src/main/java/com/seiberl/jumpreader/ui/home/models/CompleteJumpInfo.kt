package com.seiberl.jumpreader.ui.home.models

import com.seiberl.jumpreader.persistance.entities.Aircraft
import com.seiberl.jumpreader.persistance.entities.Canopy
import com.seiberl.jumpreader.persistance.entities.Dropzone
import com.seiberl.jumpreader.persistance.views.JumpMetaData

data class CompleteJumpInfo(
    val jump: JumpMetaData,
    val aircraft: Aircraft?,
    val canopy: Canopy?,
    val dropzone: Dropzone?
)
