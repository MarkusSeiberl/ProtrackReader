package com.seiberl.protrackreader.ui.jumpimport.models

import android.util.Log
import com.seiberl.protrackreader.persistance.entities.Jump
import com.seiberl.protrackreader.ui.jumpimport.models.jumpfile.JumpFile
import java.io.File

private const val TAG = "JumpFileParser"

class JumpFileParser {

    fun parse(jumpFile: File): Jump {
        val lines = jumpFile.readLines()
        val jumpFileData = JumpFile()
        Log.d(TAG, "Jump file has ${lines.size} lines.")

        jumpFileData.parseFileContent(lines)
        Log.d(TAG, "Jump file successfully parsed.")
        return jumpFileData.jump
    }
}