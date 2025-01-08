package com.seiberl.protrackreader.ui.home.models

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.util.Log
import com.seiberl.protrackreader.persistance.views.JumpMetaData
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.io.FileOutputStream
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class JumpLogPdfCreator @Inject constructor(
    @ApplicationContext context: Context
) {

    private val externalFilesDir = context.getExternalFilesDir(null)

    fun createJumpLogPdf(jumps: List<JumpMetaData>): File {
        val pdfDocument = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(842, 595, 1).create()
        val page = pdfDocument.startPage(pageInfo)
        val canvas: Canvas = page.canvas

        val paint = Paint()
        paint.textSize = 10f

        val startX = 50f
        val startY = 50f
        val cellHeight = 30f
        val columnWidths = listOf(50f, 80f, 120f, 80f, 80f, 80f, 70f, 70f, 120f, 120f)

        val headers = listOf(
            "Sprung-Nr.", "Datum", "Luftfahrzeug", "Fallschirm", "Landeort",
            "Bodenwind", "Absprunghöhe", "Freifallzeit", "Bemerkungen", "Unterschrift"
        )

        var currentX = startX
        for (i in headers.indices) {
            canvas.drawText(headers[i], currentX + 5, startY + 20, paint)
            currentX += columnWidths[i]
        }

        canvas.drawLine(startX, startY + cellHeight, startX + columnWidths.sum(), startY + cellHeight, paint)


        var currentY = startY + cellHeight
        for (jump in jumps) {
            currentX = startX

            val formattedDate = DateTimeFormatter.ofPattern("dd.MM.yyyy")
                .withZone(ZoneId.systemDefault())
                .format(jump.timestamp)

            val row = listOf(
                jump.number.toString(),
                formattedDate,
                "",
                "",
                "",
                "",
                jump.exitAltitude.toString(),
                jump.freefallTime.toString(),
                "",
                ""
            )

            for (cell in row) {
                canvas.drawText(cell, currentX + 5, currentY + 20, paint)
                currentX += columnWidths[row.indexOf(cell)]
            }

            currentY += cellHeight
            canvas.drawLine(startX, currentY, startX + columnWidths.sum(), currentY, paint)
        }

        currentX = startX
        for (width in columnWidths) {
            canvas.drawLine(currentX, startY, currentX, currentY, paint)
            currentX += width
        }
        canvas.drawLine(startX + columnWidths.sum(), startY, startX + columnWidths.sum(), currentY, paint)

        pdfDocument.finishPage(page)

        val filePath = File(externalFilesDir, "Sprungbuch.pdf")
        try {
            pdfDocument.writeTo(FileOutputStream(filePath))
            Log.d("JumpLogPdfCreator","PDF erfolgreich gespeichert: ${filePath.absolutePath}")
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            pdfDocument.close()
        }

        return filePath
    }

}