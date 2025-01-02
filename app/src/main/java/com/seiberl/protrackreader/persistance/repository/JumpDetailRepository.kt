package com.seiberl.protrackreader.persistance.repository

import com.seiberl.protrackreader.persistance.dao.JumpDao
import javax.inject.Inject

class JumpDetailRepository @Inject constructor(
    private val jumpDao: JumpDao
) {
    fun loadJump(jumpNr: Int) = jumpDao.getJumpByNumber(jumpNr)
}