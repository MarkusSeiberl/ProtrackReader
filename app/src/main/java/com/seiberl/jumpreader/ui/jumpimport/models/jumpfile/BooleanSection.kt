package com.seiberl.jumpreader.ui.jumpimport.models.jumpfile

open class BooleanSection(lineNumber: Int) : Section<Boolean>(lineNumber) {

    override var value: Boolean = true
    override fun readValue(lines: List<String>) {
        value = lines[lineNumber].toBoolean()
    }
}