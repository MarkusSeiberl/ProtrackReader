package com.seiberl.jumpreader.ui.home.models

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface.DEFAULT_BOLD
import android.graphics.pdf.PdfDocument
import com.seiberl.jumpreader.persistance.views.JumpMetaData
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import kotlin.math.max
import kotlin.math.roundToInt

private const val PADDING_START = 30f
private const val PADDING_TOP = 30f
private const val PADDING_END = 30f
private const val PADDING_BOTTOM = 30f
private val REGEX_CRLF = Regex("\r\n|\n|\r")

class PageCreator(
    private val pageWidth: Int,
    private val pageHeight: Int,
    private val headers: List<String>,
    private val columnWidths: List<Float>,
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
        require(columnWidths.sum().roundToInt() == 782) { "Width was ${columnWidths.sum().roundToInt()} but should be 782"}
    }

    fun create(): PdfDocument.Page = page


    private fun drawHeader(canvas: Canvas): Float {
        val startX = PADDING_START
        val startY = PADDING_TOP
        val endX = pageWidth.toFloat() - PADDING_END
        val cell = CellDimensions(fontSizeTitle)
        val paint = Paint().apply {
            color = Color.BLACK
            textSize = fontSizeTitle
            typeface = DEFAULT_BOLD
            letterSpacing = 0.06f
        }

        drawHorizontalLine(canvas, startX, startY, endX)

        var currentX = startX
        val currentY = startY + cell.textPositionY
        for (i in headers.indices) {
            canvas.drawText(headers[i], currentX + cell.startPadding, currentY, paint)
            drawVerticalLine(canvas, currentX, startY, startY + cell.height)
            currentX += columnWidths[i]
        }
        drawVerticalLine(canvas, currentX, startY, startY + cell.height)
        drawHorizontalLine(canvas, startX, startY + cell.height, endX)

        return startX + cell.height
    }

    fun canAddJump(jump: CompleteJumpInfo): Boolean {
        val cell = CellDimensions(fontSizeBody)
        val paint = Paint().apply {
            color = Color.BLACK
            textSize = cell.fontSize
            letterSpacing = 0.04f
        }

        val jumpData = jump.toList()
        val maxLines = jumpData
            .mapIndexed { index, data -> createTextLines(data, columnWidths[index], paint) }
            .maxOf { it.size }

        repeat((maxLines-1)) { cell.addLine() }

        val remainingHeight = pageHeight - currentY - PADDING_BOTTOM

        return remainingHeight >= cell.height
    }

    fun addJump(jump: CompleteJumpInfo) {
        currentY = drawJump(canvas, currentY, jump)
        drawHorizontalLine(canvas, PADDING_START, currentY, pageWidth.toFloat() - PADDING_END)
    }

    private fun drawJump(canvas: Canvas, startY: Float, jump: CompleteJumpInfo): Float {
        val cell = CellDimensions(fontSizeBody)
        var currentX = PADDING_START
        val textPositionY = startY + cell.textPositionY
        var endPositionY = textPositionY

        val jumpValues = jump.toList()
        jumpValues.forEachIndexed { index, value ->
            val cellWidth = columnWidths[index] - cell.startPadding - cell.endPadding
            val newY = drawMultiLineText(canvas, value, currentX, textPositionY, cellWidth, cell)
            endPositionY = max(endPositionY, newY)
            currentX += columnWidths[index]
        }

        currentX = PADDING_START
        endPositionY += cell.bottomPadding
        drawVerticalLine(canvas, currentX, startY, endPositionY)
        columnWidths.forEach { width ->
            currentX += width
            drawVerticalLine(canvas, currentX, startY, endPositionY)
        }

        return endPositionY
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

        val lines = createTextLines(text, width, paint)

        lines.forEach { line ->
            canvas.drawText(line, xAxis + cell.startPadding, currentY, paint)
            if (line != lines.last()) {
                currentY += cell.linePadding + cell.fontSize
            }
        }

        return currentY
    }

    private fun createTextLines(text: String, width: Float, paint: Paint): List<String> {
        val lines = mutableListOf<String>()
        val userLines = text.split(REGEX_CRLF)

        userLines.forEach { userLine ->

            val words = userLine.split(" ")
            val iterator = words.iterator()
            var line = iterator.next()
            while (iterator.hasNext()) {
                val word = iterator.next().trim()
                val textWidth = paint.measureText("$line$word ")
                if (textWidth > width) {
                    lines.add(line)
                    line = ""
                }
                line += "$word "
            }
            lines.add(line)
        }


        return lines
    }

    private fun drawHorizontalLine(canvas: Canvas, startX: Float, yAxis: Float, endX: Float) =
        drawLine(canvas, startX, yAxis, endX, yAxis)

    private fun drawVerticalLine(canvas: Canvas, xAxis: Float, startY: Float, endY: Float) =
        drawLine(canvas, xAxis, startY, xAxis, endY)

    private fun drawLine(canvas: Canvas, startX: Float, startY: Float, endX: Float, endY: Float) {
        val paint = Paint()
        paint.color = Color.LTGRAY
        paint.strokeWidth = 0.5f
        canvas.drawLine(startX, startY, endX, endY, paint)
    }
}

fun CompleteJumpInfo.toList(): List<String> {
    val formattedDate = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)
                .withZone(ZoneId.systemDefault())
                .format(jump.timestamp)

    return listOf(
        jump.number.toString(),
        formattedDate,
        aircraft?.name ?: "",
        (canopy?.name?.plus(" - ") ?: "") + (canopy?.size ?: ""),
        (dropzone?.icao?.plus(" - ") ?: "") + (dropzone?.name ?: ""),
        "",
        jump.exitAltitude.toString(),
        jump.freefallTime.toString(),
        "",
        ""
    )
}