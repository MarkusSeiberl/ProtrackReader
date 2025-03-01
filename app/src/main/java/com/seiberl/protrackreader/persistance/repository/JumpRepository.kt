package com.seiberl.protrackreader.persistance.repository

import com.seiberl.protrackreader.persistance.JumpFileManager
import com.seiberl.protrackreader.persistance.dao.AircraftDao
import com.seiberl.protrackreader.persistance.dao.CanopyDao
import com.seiberl.protrackreader.persistance.dao.DropzoneDao
import com.seiberl.protrackreader.persistance.dao.JumpDao
import com.seiberl.protrackreader.persistance.entities.Jump
import com.seiberl.protrackreader.persistance.views.JumpMetaData
import com.seiberl.protrackreader.ui.profile.Favorites
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class JumpRepository @Inject constructor(
    private val jumpDao: JumpDao,
    private val aircraftDao: AircraftDao,
    private val canopyDao: CanopyDao,
    private val dropzoneDao: DropzoneDao,
    private val jumpFileManager: JumpFileManager
) {
    fun observeJumpMetaData(): Flow<List<JumpMetaData>> = jumpDao.observeJumpMetaData()

    fun observeJumpNumbers(): Flow<List<Int>> = jumpDao.observeJumpNumbers()

    fun observeFavorites(): Flow<Favorites> {
        val favoriteAircraft = aircraftDao.observeFavorite()
        val favoriteCanopy = canopyDao.observeFavorite()
        val favoriteDropzone = dropzoneDao.observeFavorite()

        return combine(favoriteAircraft, favoriteCanopy, favoriteDropzone) { aircraft, canopy, dropzone ->
            Favorites(aircraft, canopy, dropzone)
        }
    }

    fun insertJumpNumbers(recordedJumpData: List<Jump>, duplicateJumpData: List<Jump>) {
        jumpDao.insert(recordedJumpData)
        updateJumps(duplicateJumpData)
    }

    fun insertJump(recordedJumpData: Jump) = jumpDao.insert(recordedJumpData)


    private fun updateJumps(incomingJumps: List<Jump>) {
        val outdatedJumps = jumpDao.getFilteredJumps(incomingJumps.map { it.number })
        val updatedJumps = incomingJumps.map { incomingJump ->
            val outdatedJump = outdatedJumps.find { it.number == incomingJump.number }
            incomingJump.copy(id = outdatedJump!!.id)
        }
        jumpDao.update(updatedJumps)
    }

    fun removeJumps(selectedJumps: List<Int>) {
        selectedJumps.forEach { jumpNumber ->
            if (jumpFileManager.deleteJumpFile(jumpNumber)) {
                jumpDao.deleteByNumber(jumpNumber)
            }
        }
    }
}