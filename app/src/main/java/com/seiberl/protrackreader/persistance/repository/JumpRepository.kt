package com.seiberl.protrackreader.persistance.repository

import com.seiberl.protrackreader.persistance.dao.JumpDao
import com.seiberl.protrackreader.persistance.entities.Jump
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class JumpRepository @Inject constructor(
    private val jumpDao: JumpDao
) {
    fun observeAll(): Flow<List<Jump>> = jumpDao.observeAll()

}