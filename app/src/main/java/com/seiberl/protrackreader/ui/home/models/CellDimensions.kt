package com.seiberl.protrackreader.ui.home.models

class CellDimensions(val fontSize: Float) {
    private var nrOfLines = 1

    val topPadding = fontSize * 0.3f
    val linePadding = fontSize * 0.2f
    val bottomPadding = fontSize * 0.4f
    val startPadding = fontSize * 0.2f
    val endPadding = fontSize * 0.2f

    val height: Float
        get() = (fontSize * nrOfLines) +
                topPadding +
                bottomPadding +
                (linePadding * (nrOfLines - 1))

    val textPositionY = height - bottomPadding

    fun addLine() {
        nrOfLines += 1
    }
}