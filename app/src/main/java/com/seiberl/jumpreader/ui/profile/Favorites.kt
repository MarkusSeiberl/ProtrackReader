package com.seiberl.jumpreader.ui.profile

import com.seiberl.jumpreader.persistance.entities.Aircraft
import com.seiberl.jumpreader.persistance.entities.Canopy
import com.seiberl.jumpreader.persistance.entities.Dropzone

data class Favorites(val aircraft: Aircraft?, val canopy: Canopy?, val dropzone: Dropzone?) {

    private val favorites = listOf(aircraft, canopy, dropzone)

    fun noneSet(): Boolean = favorites.all { it == null }
    fun oneSet(): Boolean = favorites.count { it == null } == 1
}