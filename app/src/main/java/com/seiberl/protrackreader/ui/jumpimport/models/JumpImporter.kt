package com.seiberl.protrackreader.ui.jumpimport.models

import android.content.Context
import android.util.Log
import com.seiberl.protrackreader.persistance.repository.JumpRepository
import com.seiberl.protrackreader.ui.jumpimport.models.jumpfile.JumpFile
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import java.io.File
import javax.inject.Inject


private const val TAG = "JumpImporter"

class JumpImporter @Inject constructor(
    private val jumpRepository: JumpRepository,
    private val jumpFileReader: JumpFileReader,
    @ApplicationContext context: Context
) {

    private val externalFilesDir = context.getExternalFilesDir(null)

    private val _currentJumpImport: MutableStateFlow<Int> = MutableStateFlow(-1)
    val currentJumpImport: StateFlow<Int> = _currentJumpImport

    fun importJumps(newJumps: List<Int>, duplicateJumps: List<Int>) {
        overrideJumps(duplicateJumps)
        importNewJumps(newJumps)
    }

    private fun overrideJumps(duplicateJumps: List<Int>) {
        Log.d(TAG, "Overriding no jumps. Currently not supported.")
    }

    private fun importNewJumps(newJumps: List<Int>) {
        for (jumpNr in newJumps) {
            Log.d(TAG, "Importing jump $jumpNr")
            _currentJumpImport.update { jumpNr }
            val jumpFileContent = jumpFileReader.readStoredJump(jumpNr)

            jumpRepository.insertJump(jumpFileContent.jump)
            storeJumpFile(jumpFileContent) // TODO do someting with the result
        }
    }

    private fun storeJumpFile(jumpFileContent: JumpFile): Boolean {
        val jumpFile = File(externalFilesDir, "${jumpFileContent.jump.number}.txt")

        if (jumpFile.exists() && jumpFile.isFile) {
            return false // File already exists
        }
        jumpFile.createNewFile()

        for (jumpFileLine in jumpFileContent.fileContent) {
            jumpFile.appendText(jumpFileLine)
        }

        return true
    }


}