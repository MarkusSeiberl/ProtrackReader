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

private const val MAX_JUMPS_PER_PAGE = 10

class JumpLogPdfCreator @Inject constructor(
    @ApplicationContext context: Context
) {

    private val externalFilesDir = context.getExternalFilesDir(null)

    fun createJumpLogPdf(jumps: List<JumpMetaData>): File {
        val pdfDocument = PdfDocument()

        val sortedJumps = jumps.sortedBy { it.number }

        var pdfPage = 1
        val pageCreator = PageCreator(pdfDocument, PAGE_WIDTH, PAGE_HEIGHT, pdfPage)
        val iterator = sortedJumps.iterator()

        while (iterator.hasNext()) {
            val jump = iterator.next()
            if (pageCreator.canAddJump(jump)) {
                pageCreator.addJump(jump)
            } else {
                val page = pageCreator.createPage()
                pdfDocument.finishPage(page)
                pdfPage += 1
                pageCreator.reset(pdfPage)
                pageCreator.addJump(jump)
            }
        }
        val lastPage = pageCreator.createPage()
        pdfDocument.finishPage(lastPage)



//        val jumpPages = sortedJumps.chunked(MAX_JUMPS_PER_PAGE)
//
//        jumpPages.forEachIndexed { index, pageJumps ->
//            val pdfPage = index + 1 // +1 because index starts at 0
//            val pageCreator = PageCreator(pdfDocument, PAGE_WIDTH, PAGE_HEIGHT, index, pageJumps)
//            val page = pageCreator.createPage()
//            pdfDocument.finishPage(page)
//        }

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

    private fun paginateJumps(jumps: List<JumpMetaData>): List<List<JumpMetaData>> {
        val pages = mutableListOf<List<JumpMetaData>>()
        var currentPageJumps = mutableListOf<JumpMetaData>()
        var currentHeight = 0f

        for (jump in jumps) {
            val jumpHeight = measureJumpHeight(jump)

            // Check if the jump fits in the remaining space
            if (currentHeight + jumpHeight > pageHeight) {
                // Save the current page and start a new one
                pages.add(currentPageJumps)
                currentPageJumps = mutableListOf()
                currentHeight = 0f
            }

            // Add the jump to the current page
            currentPageJumps.add(jump)
            currentHeight += jumpHeight
        }

        // Add the last page if it contains any jumps
        if (currentPageJumps.isNotEmpty()) {
            pages.add(currentPageJumps)
        }

        return pages
    }

}