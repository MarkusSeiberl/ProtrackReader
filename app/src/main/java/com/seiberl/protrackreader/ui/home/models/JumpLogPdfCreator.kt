package com.seiberl.protrackreader.ui.home.models

import android.content.Context
import android.graphics.pdf.PdfDocument
import android.util.Log
import com.seiberl.protrackreader.persistance.views.JumpMetaData
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

private const val PAGE_WIDTH = 842
private const val PAGE_HEIGHT = 595

class JumpLogPdfCreator @Inject constructor(
    @ApplicationContext context: Context
) {

    private val externalFilesDir = context.getExternalFilesDir(null)

    fun createJumpLogPdf(jumps: List<JumpMetaData>): File {
        val pdfDocument = PdfDocument()

        val sortedJumps = jumps.sortedBy { it.number }

        var pdfPage = 1
        var pageCreator = PageCreator(PAGE_WIDTH, PAGE_HEIGHT, pdfDocument, pdfPage)
        val iterator = sortedJumps.iterator()

        while (iterator.hasNext()) {
            val jump = iterator.next()
            if (pageCreator.canAddJump(jump)) {
                pageCreator.addJump(jump)
            } else {
                pdfDocument.finishPage(pageCreator.create())
                pdfPage += 1
                pageCreator = PageCreator(PAGE_WIDTH, PAGE_HEIGHT, pdfDocument, pdfPage)
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