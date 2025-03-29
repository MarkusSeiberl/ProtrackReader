package com.seiberl.jumpreader.persistance.repository

import com.seiberl.jumpreader.persistance.dao.JumpDao
import javax.inject.Inject

class JumpDetailRepository @Inject constructor(
    private val jumpDao: JumpDao
) {
    fun loadJump(jumpNr: Int) = jumpDao.getJumpByNumber(jumpNr)
}