package com.seiberl.protrackreader.ui.jumpimport.models

import com.seiberl.protrackreader.persistance.entities.Jump
import com.seiberl.protrackreader.ui.jumpimport.models.jumpfile.JumpFile
import java.io.File

class JumpFileParser {

    fun parse(jumpFile: File): Jump {
        val lines = jumpFile.readLines()
        val jumpFileData = JumpFile()
        jumpFileData.parseFileContent(lines)
        return jumpFileData.jump
    }

}