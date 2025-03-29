package com.seiberl.jumpreader.persistance.repository

import com.seiberl.jumpreader.persistance.dao.CanopyDao
import com.seiberl.jumpreader.persistance.entities.Canopy
import javax.inject.Inject

class CanopyRepository @Inject constructor(
    private val canopyDao: CanopyDao
) {
    fun observe() = canopyDao.observeAll()

    fun add(canopy: Canopy) {
        if (canopy.favorite) {
            disfavorExistingAircraft()
        }
        canopyDao.insert(canopy)
    }

    fun star(canopy: Canopy) {
        disfavorExistingAircraft()
        canopyDao.star(canopy.id, !canopy.favorite)
    }

    private fun disfavorExistingAircraft() {
        val favorites = canopyDao.getFavorites().map { it.copy(favorite = false) }
        canopyDao.update(favorites)
    }

    fun remove(canopy: Canopy) {
        canopyDao.delete(canopy)
    }
}