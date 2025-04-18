package com.seiberl.jumpreader.persistance.repository

import com.seiberl.jumpreader.persistance.dao.DropzoneDao
import com.seiberl.jumpreader.persistance.entities.Dropzone
import javax.inject.Inject

class DropzoneRepository @Inject constructor(
    private val dropzoneDao: DropzoneDao
) {
    fun observe() = dropzoneDao.observeAll()

    fun add(dropzone: Dropzone) {
        if (dropzone.favorite) {
            disfavorExistingAircraft()
        }
        dropzoneDao.insert(dropzone)
    }

    fun star(dropzone: Dropzone) {
        disfavorExistingAircraft()
        dropzoneDao.star(dropzone.id, !dropzone.favorite)
    }

    private fun disfavorExistingAircraft() {
        val favorites = dropzoneDao.getFavorites().map { it.copy(favorite = false) }
        dropzoneDao.update(favorites)
    }

    fun remove(dropzone: Dropzone) {
        dropzoneDao.delete(dropzone)
    }
}