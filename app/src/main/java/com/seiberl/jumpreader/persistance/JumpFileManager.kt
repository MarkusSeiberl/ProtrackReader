package com.seiberl.jumpreader.persistance

import android.content.Context
import com.seiberl.jumpreader.ui.jumpimport.models.jumpfile.JumpFile
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import javax.inject.Inject

class JumpFileManager @Inject constructor(
    @ApplicationContext context: Context
) {
    private val externalFilesDir = context.getExternalFilesDir(null)

    fun storeJumpFile(jumpFileContent: JumpFile): Boolean {
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

    /**
     * Deletes the jump file with the given jump number. Returns true if the file was deleted
     * successfully or if it did not exist. Returns false if the file could not be deleted.
     */
    fun deleteJumpFile(jumpNumber: Int): Boolean {
        val jumpFile = File(externalFilesDir, "$jumpNumber.txt")
        return if (jumpFile.exists() && jumpFile.isFile) {
            jumpFile.delete()
        } else {
            true
        }
    }
}