package com.seiberl.protrackreader.persistance.repository

import com.seiberl.protrackreader.persistance.dao.DropzoneDao
import javax.inject.Inject

class DropzoneRepository @Inject constructor(
    private val dropzoneDao: DropzoneDao
) {
    fun observeDropzone() = dropzoneDao.observeAll()
}