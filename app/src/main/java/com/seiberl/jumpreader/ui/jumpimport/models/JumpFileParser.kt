package com.seiberl.jumpreader.ui.jumpimport.models

import android.util.Log
import com.seiberl.jumpreader.ui.jumpimport.models.jumpfile.JumpFile
import java.io.File

private const val TAG = "JumpFileParser"

class JumpFileParser {

    fun parse(jumpFile: File): JumpFile {
        val lines = jumpFile.readLines()
        val jumpFileData = JumpFile()
        Log.d(TAG, "Jump file has ${lines.size} lines.")

        jumpFileData.parseFileContent(lines)
        Log.d(TAG, "Jump file successfully parsed.")
        return jumpFileData
    }
}