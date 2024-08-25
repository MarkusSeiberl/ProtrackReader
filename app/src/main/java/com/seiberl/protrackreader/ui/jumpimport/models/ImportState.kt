package com.seiberl.protrackreader.ui.jumpimport.models

enum class ImportState {
    PERMISSION_REQUIRED,
    PERMISSION_DENIED,
    SEARCHING_VOLUME,
    SEARCHING_VOLUME_FAILED,
    VOLUME_EMPTY,
    IMPORT_READY,
    IMPORT_START_FAILED,
    IMPORT_ONGOING,
    IMPORT_SUCCESSFUL,
    IMPORT_FAILED
}