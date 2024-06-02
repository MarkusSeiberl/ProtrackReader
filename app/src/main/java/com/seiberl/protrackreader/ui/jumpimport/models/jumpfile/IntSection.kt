package com.seiberl.protrackreader.ui.jumpimport.models.jumpfile

open class IntSection(lineNumber: Int): Section<Int>(lineNumber) {

    override var value: Int = 0
    override fun readValue(lines: List<String>) {
        value = lines[lineNumber].toInt()
    }
}