package com.seiberl.protrackreader.ui.jumpimport.models.jumpfile

import com.google.common.primitives.Booleans

open class BooleanSection(lineNumber: Int) : Section<Boolean>(lineNumber) {

    override var value: Boolean = true
    override fun readValue(lines: List<String>) {
        value = lines[lineNumber].toBoolean()
    }
}