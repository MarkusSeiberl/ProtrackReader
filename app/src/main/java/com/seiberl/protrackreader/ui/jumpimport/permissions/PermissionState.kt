package com.seiberl.protrackreader.ui.jumpimport.permissions

internal data class PermissionState(
    val permissionGranted: Boolean = false,
    val permissionRequested: Boolean = permissionGranted
)