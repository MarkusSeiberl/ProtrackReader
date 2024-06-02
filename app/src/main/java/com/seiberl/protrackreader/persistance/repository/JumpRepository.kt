package com.seiberl.protrackreader.persistance.repository

import com.seiberl.protrackreader.persistance.dao.JumpDao
import com.seiberl.protrackreader.persistance.entities.Jump
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class JumpRepository @Inject constructor(
    private val jumpDao: JumpDao
) {
    fun observeAll(): Flow<List<Jump>> = jumpDao.observeAll()

    fun observeJumpNumbers(): Flow<List<Int>> = jumpDao.observeJumpNumbers()
    fun insertJumpNumbers(recordedJumpData: List<Jump>, duplicateJumpData: List<Jump>) {
        jumpDao.insert(recordedJumpData)
        updateJumps(duplicateJumpData)
    }

    private fun updateJumps(incomingJumps: List<Jump>) {
        val outdatedJumps = jumpDao.getFilteredJumps(incomingJumps.map { it.number })
        val updatedJumps = incomingJumps.map { incomingJump ->
            val outdatedJump = outdatedJumps.find { it.number == incomingJump.number }
            incomingJump.copy(id = outdatedJump!!.id)
        }
        jumpDao.update(updatedJumps)
    }
}