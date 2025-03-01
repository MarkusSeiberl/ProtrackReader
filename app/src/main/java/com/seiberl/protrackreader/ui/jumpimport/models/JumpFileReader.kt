package com.seiberl.protrackreader.ui.jumpimport.models

import android.content.Context
import android.os.storage.StorageManager
import android.os.storage.StorageVolume
import android.util.Log
import com.seiberl.protrackreader.ui.jumpimport.models.exceptions.VolumeAccessException
import com.seiberl.protrackreader.ui.jumpimport.models.jumpfile.JumpFile
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

private const val PROTRACK_VOLUME_DESCRIPTION = "PROTRACK2"
private const val JUMP_FILE_REGEX = "^\\d+\\.txt\$"

private const val TAG = "JumpFileReader"

@Singleton
class JumpFileReader @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private lateinit var protrackVolume: StorageVolume

    fun findProtrackVolume(): Boolean {
        val storageManager = context.getSystemService(Context.STORAGE_SERVICE) as StorageManager
        val volumes = storageManager.storageVolumes
        val storageVolume = volumes.find {
            it.getDescription(context).contains(PROTRACK_VOLUME_DESCRIPTION) || it.isPrimary.not()
        }

        if (storageVolume == null || storageVolume.directory == null) {
            Log.w(TAG, "Could not find protrack volume")
            return false
        }

        Log.d(TAG, "Found protrack volume: ${storageVolume.getDescription(context)}")

        val storageFiles = storageVolume.directory!!.listFiles()
        if (storageFiles == null || storageFiles.isEmpty()) {
            Log.w(TAG, "Protrack volume is empty. Cannot import jumps.")
            throw VolumeAccessException("Protrack volume is empty. Cannot import jumps.")

        } else {
            this.protrackVolume = storageVolume
            return true
        }
    }

    fun readStoredJumpNumbers(): List<Int> {
        if (!::protrackVolume.isInitialized) {
            Log.w(TAG, "Protrack volume not initialized. Returning empty list.")
            return emptyList()
        }

        return fetchJumpFiles().map { file ->
            file.name.split(".")[0].toInt()
        }
    }

    fun readStoredJump(jumpNr: Int): JumpFile {
        val jumpFile = fetchJumpFile(jumpNr)
        if (jumpFile == null) {
            Log.w(TAG, "Could not find jump file with number $jumpNr")
            throw Exception("Could not find jump file with number $jumpNr")
        }
        val parser = JumpFileParser()
        return parser.parse(jumpFile)
    }

    private fun fetchJumpFile(jumpNr: Int): File? {
        val files = protrackVolume.directory?.listFiles() ?: arrayOf()
        return files.firstOrNull { file -> file.name == "$jumpNr.txt" }
    }

    private fun fetchJumpFiles(): List<File> {
        val files = protrackVolume.directory?.listFiles() ?: return emptyList()
        return files.filter { file ->
            Regex(JUMP_FILE_REGEX).matches(file.name)
        }
    }
}