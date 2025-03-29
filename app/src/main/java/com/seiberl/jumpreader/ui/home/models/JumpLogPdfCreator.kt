package com.seiberl.jumpreader.ui.home.models

import android.content.Context
import android.graphics.pdf.PdfDocument
import android.util.Log
import com.seiberl.jumpreader.R
import com.seiberl.jumpreader.persistance.views.JumpMetaData
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

private const val PAGE_WIDTH = 842
private const val PAGE_HEIGHT = 595
private val COLUMN_WIDTHS =
    listOf(40f, 58.2f, 88.2f, 88.2f, 78.2f, 78.2f, 78.2f, 78.2f, 111.4f, 83.2f)
private val HEADER_RESOURCES = listOf(
    R.string.pdf_jump_nr,
    R.string.pdf_date,
    R.string.pdf_aircraft,
    R.string.pdf_canopy,
    R.string.pdf_location,
    R.string.pdf_wind_speed,
    R.string.pdf_exit_altitude,
    R.string.pdf_freefall_time,
    R.string.pdf_notes,
    R.string.pdf_signature
)

class JumpLogPdfCreator @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private val externalFilesDir = context.getExternalFilesDir(null)
    private val localizedHeaders = HEADER_RESOURCES.map { context.getString(it) }

    fun createJumpLogPdf(jumps: List<JumpMetaData>): File {
        val pdfDocument = PdfDocument()

        val sortedJumps = jumps.sortedBy { it.number }

        var pdfPage = 1
        var pageCreator = PageCreator(
            PAGE_WIDTH,
            PAGE_HEIGHT,
            localizedHeaders,
            COLUMN_WIDTHS,
            pdfDocument,
            pdfPage
        )
        val iterator = sortedJumps.iterator()

        while (iterator.hasNext()) {
            val jump = iterator.next()
            if (pageCreator.canAddJump(jump)) {
                pageCreator.addJump(jump)
            } else {
                pdfDocument.finishPage(pageCreator.create())
                pdfPage += 1
                pageCreator = PageCreator(
                    PAGE_WIDTH,
                    PAGE_HEIGHT,
                    localizedHeaders,
                    COLUMN_WIDTHS,
                    pdfDocument,
                    pdfPage
                )
                pageCreator.addJump(jump)
            }
        }
        pdfDocument.finishPage(pageCreator.create())

        val exportFolder = File(externalFilesDir, "export")
        if (!exportFolder.exists()) {
            exportFolder.mkdirs()
        }

        val filePath = File(exportFolder, "Sprungbuch.pdf")
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