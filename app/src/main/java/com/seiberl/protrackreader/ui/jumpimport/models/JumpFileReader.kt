package com.seiberl.protrackreader.ui.jumpimport.models

import android.content.Context
import android.os.storage.StorageManager
import android.os.storage.StorageVolume
import android.util.Log
import com.seiberl.protrackreader.persistance.entities.Jump
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import javax.inject.Inject

private const val PROTRACK_VOLUME_DESCRIPTION = "PROTRACK2"
private const val JUMP_FILE_REGEX = "^\\d+\\.txt\$"

private const val TAG = "JumpFileReader"

class JumpFileReader @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private lateinit var protrackVolume: StorageVolume

    fun findProtrackVolume(): Boolean {
        val storageManager = context.getSystemService(Context.STORAGE_SERVICE) as StorageManager
        val volumes = storageManager.storageVolumes
        val protrackVolume =
            volumes.find { it.getDescription(context).contains(PROTRACK_VOLUME_DESCRIPTION) }

        if (protrackVolume != null) {
            Log.d(TAG, "Found protrack volume: ${protrackVolume.getDescription(context)}")
            this.protrackVolume = protrackVolume
            return true
        }

        Log.w(TAG, "Could not find protrack volume")

        return false
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

    fun readStoredJumps(): List<Jump> {
        val files = fetchJumpFiles()
        val parser = JumpFileParser()
        Log.d(TAG, "Found ${files.size} jump files.")

        return files.map { jumpFile ->
            Log.d(TAG, "Parsing ${jumpFile.name}.")
            parser.parse(jumpFile)
        }
    }

    private fun fetchJumpFiles(): List<File> {
        val files = protrackVolume.directory?.listFiles() ?: return emptyList()
        return files.filter { file ->
            Regex(JUMP_FILE_REGEX).matches(file.name)
        }
    }
}