package com.seiberl.protrackreader.persistance.repository

import com.seiberl.protrackreader.persistance.dao.CanopyDao
import javax.inject.Inject

class CanopyRepository @Inject constructor(
    private val canopyDao: CanopyDao
) {
    fun observeCanopy() = canopyDao.observeAll()
}