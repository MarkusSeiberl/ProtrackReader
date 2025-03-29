package com.seiberl.jumpreader.util

import android.content.Intent
import android.hardware.usb.UsbManager.EXTRA_PERMISSION_GRANTED
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import android.os.Parcelable

inline fun <reified T : Parcelable> Intent.getParcelable(name: String, clazz: Class<T>): T? =
    when {
        SDK_INT >= Build.VERSION_CODES.TIRAMISU -> this.getParcelableExtra(name, clazz)
        else ->
            @Suppress("DEPRECATION")
            this.getParcelableExtra(name)
    }

fun Intent.isPermissionGranted() = this.getBooleanExtra(EXTRA_PERMISSION_GRANTED, false)