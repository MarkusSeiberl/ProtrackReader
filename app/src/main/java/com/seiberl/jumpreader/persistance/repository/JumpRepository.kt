package com.seiberl.jumpreader.persistance.repository

import com.seiberl.jumpreader.persistance.JumpFileManager
import com.seiberl.jumpreader.persistance.dao.AircraftDao
import com.seiberl.jumpreader.persistance.dao.CanopyDao
import com.seiberl.jumpreader.persistance.dao.DropzoneDao
import com.seiberl.jumpreader.persistance.dao.JumpDao
import com.seiberl.jumpreader.persistance.entities.Aircraft
import com.seiberl.jumpreader.persistance.entities.Canopy
import com.seiberl.jumpreader.persistance.entities.Dropzone
import com.seiberl.jumpreader.persistance.entities.Jump
import com.seiberl.jumpreader.persistance.views.JumpMetaData
import com.seiberl.jumpreader.ui.profile.Favorites
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

    fun observeAircraft(): Flow<List<Aircraft>> = aircraftDao.observeAll()
    fun observeCanopies(): Flow<List<Canopy>> = canopyDao.observeAll()
    fun observeDropzone(): Flow<List<Dropzone>> = dropzoneDao.observeAll()

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

    fun updateJumpsMetaData(
        jumpNumbers: List<JumpMetaData>,
        aircraft: Aircraft?,
        canopy: Canopy?,
        dropzone: Dropzone?
    ) {
        val jumps = jumpDao.getJumpsByNumber(jumpNumbers.map { it.number })
        val updatedJumps = jumps.map { jump ->
            jump.copy(
                aircraftId = aircraft?.id,
                canopyId = canopy?.id,
                dropzoneId = dropzone?.id
            )
        }
        jumpDao.update(updatedJumps)
    }
}