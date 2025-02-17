package com.seiberl.protrackreader.ui.home.models

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.Typeface.BOLD
import android.graphics.Typeface.DEFAULT
import android.graphics.Typeface.DEFAULT_BOLD
import android.graphics.pdf.PdfDocument
import android.text.TextPaint
import com.seiberl.protrackreader.persistance.views.JumpMetaData
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import kotlin.math.max
import kotlin.math.roundToInt

private const val PADDING_START = 30f
private const val PADDING_TOP = 30f
private const val PADDING_END = 30f
private const val PADDING_BOTTOM = 30f

private val COLUMN_WIDTHS = listOf(40f, 58.2f, 88.2f, 88.2f, 78.2f, 78.2f, 78.2f, 78.2f, 111.4f, 83.2f)

class PageCreator constructor(
    private val pageWidth: Int,
    private val pageHeight: Int,
    document: PdfDocument,
    pdfPage: Int
) {

    private val pageInfo = PdfDocument.PageInfo.Builder(pageWidth, pageHeight, pdfPage).create()
    private val page = document.startPage(pageInfo)
    private val canvas: Canvas = page.canvas

    private val fontSizeTitle = FontMetrics(canvas.height).getNominalFontHeight() * TITLE_FONT_SCALE
    private val fontSizeBody = FontMetrics(canvas.height).getNominalFontHeight() * FIELD_FONT_SCALE

    private var currentY = 0f

    init {
        currentY = drawHeader(canvas)
        require(COLUMN_WIDTHS.sum().roundToInt() == 782) { "Width was ${COLUMN_WIDTHS.sum().roundToInt()} but should be 782"}
    }

    fun create(): PdfDocument.Page = page


    private fun drawHeader(canvas: Canvas): Float {
        val startX = PADDING_START
        val startY = PADDING_TOP
        val endX = pageWidth.toFloat() - PADDING_END
        drawHorizontalLine(canvas, startX, startY, endX)

        // TODO fetch from resources
        val headers = listOf(
            "Nr.", "Datum", "Luftfahrzeug", "Fallschirm", "Landeort",
            "Bodenwind", "Absprunghöhe", "Freifallzeit", "Bemerkungen", "Unterschrift"
        )
        val cell = CellDimensions(fontSizeTitle)

        val paint = Paint().apply {
            color = Color.BLACK
            textSize = fontSizeTitle
            typeface = DEFAULT_BOLD
            letterSpacing = 0.06f
        }

        var currentX = startX
        val currentY = startY + cell.textPositionY
        for (i in headers.indices) {
            canvas.drawText(headers[i], currentX + cell.startPadding, currentY, paint)
            drawVerticalLine(canvas, currentX, startY, startY + cell.height)
            currentX += COLUMN_WIDTHS[i]
        }
        drawVerticalLine(canvas, currentX, startY, startY + cell.height)
        drawHorizontalLine(canvas, startX, startY + cell.height, endX)

        return startX + cell.height
    }

    fun canAddJump(jump: JumpMetaData): Boolean {
        val cell = CellDimensions(fontSizeBody)
        val paint = Paint().apply {
            color = Color.BLACK
            textSize = cell.fontSize
            letterSpacing = 0.04f
        }

        val jumpData = jump.toList()
        val maxLines = jumpData
            .map { createLines(it, pageWidth.toFloat(), paint) }
            .maxOf { it.size }

        repeat((maxLines-1)) { cell.addLine() }

        val remainingHeight = pageHeight - currentY - PADDING_BOTTOM

        return remainingHeight >= cell.height
    }

    fun addJump(jump: JumpMetaData) {
        currentY = drawJump(canvas, currentY, jump)
    }

    private fun drawJump(canvas: Canvas, startY: Float, jump: JumpMetaData): Float {
        val cell = CellDimensions(fontSizeBody)
        var currentX = PADDING_START
        val endX = pageWidth.toFloat() - PADDING_END
        val textPositionY = startY + cell.textPositionY
        var endPositionY = textPositionY

        val jumpValues = jump.toList()
        jumpValues.forEachIndexed { index, value ->
            val cellWidth = COLUMN_WIDTHS[index] - cell.startPadding - cell.endPadding
            val newY = drawMultiLineText(canvas, value, currentX, textPositionY, cellWidth, cell)
            endPositionY = max(endPositionY, newY)
            currentX += COLUMN_WIDTHS[index]
        }

        return endPositionY + cell.bottomPadding
    }

    private fun drawMultiLineText(
        canvas: Canvas,
        text: String,
        xAxis: Float,
        textPositionY: Float,
        width: Float,
        cell: CellDimensions
    ): Float {
        var currentY = textPositionY

        if (text.isEmpty()) {
            return currentY
        }

        val paint = Paint().apply {
            color = Color.BLACK
            textSize = cell.fontSize
            letterSpacing = 0.04f
        }

        val lines = createLines(text, width, paint)

        lines.forEach { line ->
            canvas.drawText(line, xAxis + cell.startPadding, currentY, paint)
            currentY += cell.linePadding + cell.fontSize
        }

        return currentY
    }

    private fun createLines(text: String, width: Float, paint: Paint): List<String> {
        val lines = mutableListOf<String>()
        val words = text.split(" ")

        val iterator = words.iterator()
        var line = iterator.next()
        while (iterator.hasNext()) {
            val word = iterator.next()
            val textWidth = paint.measureText("$line $word")
            if (textWidth > width) {
                lines.add(line)
                line = ""
            }
            line += " $word"
        }
        lines.add(line)
        return lines
    }

    private fun drawHorizontalLine(canvas: Canvas, startX: Float, yAxis: Float, endX: Float) =
        drawLine(canvas, startX, yAxis, endX, yAxis)

    private fun drawVerticalLine(canvas: Canvas, xAxis: Float, startY: Float, endY: Float) =
        drawLine(canvas, xAxis, startY, xAxis, endY)

    private fun drawLine(canvas: Canvas, startX: Float, startY: Float, endX: Float, endY: Float) {
        val paint = Paint()
        paint.color = Color.BLACK
        paint.strokeWidth = 1f
        canvas.drawLine(startX, startY, endX, endY, paint)
    }
}

fun JumpMetaData.toList(): List<String> {
    val formattedDate = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)
                .withZone(ZoneId.systemDefault())
                .format(timestamp)

    return listOf(
        number.toString(),
        formattedDate,
        "Cessna Caravan 208",
        "Safire 2 149",
        "LOLF",
        "2 m/s",
        exitAltitude.toString(),
        freefallTime.toString(),
        "4-way jump - QMFCN",
        ""
    )
}