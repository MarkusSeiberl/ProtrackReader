package com.seiberl.protrackreader.util

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.RECEIVER_NOT_EXPORTED
import android.content.IntentFilter
import android.os.Build
import android.os.Build.VERSION.SDK_INT

@SuppressLint("UnspecifiedRegisterReceiverFlag")
fun Context.registerNotExportedReceiver(receiver: BroadcastReceiver, filter: IntentFilter) {
    when {
        SDK_INT >= Build.VERSION_CODES.TIRAMISU ->
            this.registerReceiver(receiver, filter, RECEIVER_NOT_EXPORTED)
        else ->
            this.registerReceiver(receiver, filter)
    }
}