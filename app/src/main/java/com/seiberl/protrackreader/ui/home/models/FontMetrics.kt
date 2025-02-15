package com.seiberl.protrackreader.ui.home.models


const val FONT_CANVAS_HEIGHT_RATIO = 0.02f

const val TITLE_FONT_SCALE = 1.2f
const val SUBTITLE_FONT_SCALE = 0.9f
const val FIELD_FONT_SCALE = 0.6f


class FontMetrics constructor (private val canvasHeight: Int) {

    fun getNominalFontHeight(): Int {
        return (canvasHeight * FONT_CANVAS_HEIGHT_RATIO).toInt()
    }
}
