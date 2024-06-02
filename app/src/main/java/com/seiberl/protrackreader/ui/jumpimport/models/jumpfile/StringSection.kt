package com.seiberl.protrackreader.ui.jumpimport.models.jumpfile

open class StringSection(lineNumber: Int) : Section<String>(lineNumber) {

    override lateinit var value: String

    override fun readValue(lines: List<String>) {
        value = lines[lineNumber]
    }
}