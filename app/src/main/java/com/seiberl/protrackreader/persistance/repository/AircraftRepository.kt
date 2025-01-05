package com.seiberl.protrackreader.persistance.repository

import com.seiberl.protrackreader.persistance.dao.AircraftDao
import com.seiberl.protrackreader.persistance.entities.Aircraft
import javax.inject.Inject

class AircraftRepository @Inject constructor(
    private val aircraftDao: AircraftDao
) {
    fun observeAircraft() = aircraftDao.observeAll()

    fun addAircraft(aircraft: Aircraft) {
        if (aircraft.favorite) {
            disfavorExistingAircraft()
        }
        aircraftDao.insert(aircraft)
    }

    fun starAircraft(aircraft: Aircraft) {
        disfavorExistingAircraft()
        aircraftDao.starAircraft(aircraft.id, !aircraft.favorite)
    }

    private fun disfavorExistingAircraft() {
        val favorites = aircraftDao.getFavorites().map { it.copy(favorite = false) }
        aircraftDao.update(favorites)
    }

    fun remove(aircraft: Aircraft) {
        aircraftDao.delete(aircraft)
    }
}