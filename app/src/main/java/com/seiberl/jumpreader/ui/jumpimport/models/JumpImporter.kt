package com.seiberl.jumpreader.ui.jumpimport.models

import android.util.Log
import com.seiberl.jumpreader.persistance.JumpFileManager
import com.seiberl.jumpreader.persistance.repository.JumpRepository
import com.seiberl.jumpreader.ui.profile.Favorites
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject


private const val TAG = "JumpImporter"

class JumpImporter @Inject constructor(
    private val jumpRepository: JumpRepository,
    private val jumpFileReader: JumpFileReader,
    private val jumpFileManager: JumpFileManager
) {

    private val _currentJumpImport: MutableStateFlow<Int> = MutableStateFlow(-1)
    val currentJumpImport: StateFlow<Int> = _currentJumpImport

    private val _jumpImportCount: MutableStateFlow<Int> = MutableStateFlow(0)
    val jumpImportCount: StateFlow<Int> = _jumpImportCount

    fun importJumps(newJumps: List<Int>, duplicateJumps: List<Int>, favorites: Favorites) {
        overrideJumps(duplicateJumps, favorites)
        importNewJumps(newJumps, favorites)
    }

    private fun overrideJumps(duplicateJumps: List<Int>, favorites: Favorites) {
        Log.d(TAG, "Overriding no jumps. Currently not supported.")
    }

    private fun importNewJumps(newJumps: List<Int>, favorites: Favorites) {
        for (jumpNr in newJumps) {
            Log.d(TAG, "Importing jump $jumpNr")
            _currentJumpImport.update { jumpNr }
            val jumpFileContent = jumpFileReader.readStoredJump(jumpNr)

            if (jumpFileManager.storeJumpFile(jumpFileContent)) {
                val jump = jumpFileContent.jump
                val jump1 = jump.copy(
                    aircraftId = favorites.aircraft?.id,
                    canopyId = favorites.canopy?.id,
                    dropzoneId = favorites.dropzone?.id
                )
                jumpRepository.insertJump(jump1)
                _jumpImportCount.update { it + 1 }
            }
        }
    }
}