package com.seiberl.protrackreader.persistance.converter

import androidx.room.TypeConverter
import java.time.Instant

class Converter {
    @TypeConverter
    fun fromTimestampToInstant(timestamp: Long?): Instant? {
        return timestamp?.let { Instant.ofEpochMilli(it) }
    }

    @TypeConverter
    fun fromInstantToTimestamp(instant: Instant?): Long? {
        return instant?.toEpochMilli()
    }

    @TypeConverter
    fun fromStringToIntArray(arrayString: String?): IntArray {
        if (arrayString.isNullOrEmpty()) return intArrayOf()
        return arrayString
            .removeSurrounding("[", "]")
            .replace(" ", "")
            .split(",")
            .map { it.toInt() }
            .toIntArray()
    }

    @TypeConverter
    fun fromIntArrayToString(array: IntArray?): String {
        return array?.contentToString() ?: ""
    }
}

