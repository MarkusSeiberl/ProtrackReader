package com.seiberl.protrackreader.persistance.repository

import com.seiberl.protrackreader.persistance.dao.AircraftDao
import com.seiberl.protrackreader.persistance.entities.Aircraft
import javax.inject.Inject

class AircraftRepository @Inject constructor(
    private val aircraftDao: AircraftDao
) {
    fun observeAircraft() = aircraftDao.observeAll()

    fun addAircraft(aircraft: Aircraft) = aircraftDao.insert(aircraft)

    fun starAircraft(aircraft: Aircraft) = aircraftDao.starAircraft(aircraft.id, !aircraft.favorite)
}