package com.seiberl.protrackreader.persistance.repository

import com.seiberl.protrackreader.persistance.dao.AircraftDao
import com.seiberl.protrackreader.persistance.entities.Aircraft
import javax.inject.Inject

class AircraftRepository @Inject constructor(
    private val aircraftDao: AircraftDao
) {
    fun observe() = aircraftDao.observeAll()

    fun add(aircraft: Aircraft) {
        if (aircraft.favorite) {
            disfavorExistingAircraft()
        }
        aircraftDao.insert(aircraft)
    }

    fun star(aircraft: Aircraft) {
        disfavorExistingAircraft()
        aircraftDao.star(aircraft.id, !aircraft.favorite)
    }

    private fun disfavorExistingAircraft() {
        val favorites = aircraftDao.getFavorites().map { it.copy(favorite = false) }
        aircraftDao.update(favorites)
    }

    fun remove(aircraft: Aircraft) {
        aircraftDao.delete(aircraft)
    }
}