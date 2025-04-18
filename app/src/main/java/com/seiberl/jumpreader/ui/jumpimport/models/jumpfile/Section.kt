package com.seiberl.jumpreader.ui.jumpimport.models.jumpfile

sealed class Section<T>(
    protected val lineNumber: Int
) {
    abstract var value: T
    abstract fun readValue(lines: List<String>)
}