package com.seiberl.protrackreader.ui.jumpimport.models.jumpfile

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

open class DateTimeSection(lineNumber: Int) : Section<Instant>(lineNumber) {

    override lateinit var value: Instant

    object DateSection : StringSection(6)
    object TimeSection : StringSection(7)

    override fun readValue(lines: List<String>) {
        DateSection.readValue(lines)
        TimeSection.readValue(lines)
        val dateString = DateSection.value
        val timeString = TimeSection.value
        val format = DateTimeFormatter.ofPattern("yyyyMMddHHmmss")
        val localDateTime = LocalDateTime.parse("$dateString$timeString", format)

        value = localDateTime.toInstant(ZoneOffset.UTC)
    }
}