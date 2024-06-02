package com.seiberl.protrackreader.ui.jumpimport.models.jumpfile

open class IntArraySection(lineNumber: Int) : Section<IntArray>(lineNumber) {

    override lateinit var value: IntArray
    override fun readValue(lines: List<String>) {
        val endIndex = lines.size - 2
        val concatenatedStrings = lines.subList(lineNumber, endIndex).joinToString("")
        val singleStringValues = concatenatedStrings.split(",")
        value = singleStringValues.map { it.trim().toInt() }.toIntArray()
    }
}