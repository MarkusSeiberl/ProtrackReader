package com.seiberl.protrackreader.ui.jumpimport.permissions



import android.app.PendingIntent
import android.app.PendingIntent.FLAG_MUTABLE
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.USB_SERVICE
import android.content.Intent
import android.content.IntentFilter
import android.hardware.usb.UsbDevice
import android.hardware.usb.UsbManager
import android.hardware.usb.UsbManager.EXTRA_DEVICE
import android.util.Log
import com.seiberl.protrackreader.util.getParcelable
import com.seiberl.protrackreader.util.isPermissionGranted
import com.seiberl.protrackreader.util.registerNotExportedReceiver

private const val ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION"

internal class UsbPermissions(private val context: Context) {

    private val usbManager: UsbManager = context.getSystemService(USB_SERVICE) as UsbManager
    private val devicePermissionsState = mutableMapOf<UsbDevice, PermissionState>()

    private var callback: ((success: Boolean) -> Unit)? = null

    private val usbPermissionReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (ACTION_USB_PERMISSION == intent.action) {
                synchronized(this) {
                    val device = intent.getParcelable(EXTRA_DEVICE, UsbDevice::class.java)

                    device?.apply {
                        devicePermissionsState[this] =
                            PermissionState(intent.isPermissionGranted(), true)
                    }

                    if (intent.isPermissionGranted()) {
                        Log.i("UsbPermissions", "Permission granted for device: ${device?.deviceName}.")
                    } else {
                        Log.i("UsbPermissions", "Permission denied for device: ${device?.deviceName}.")
                    }
                }
                requestPermission()
            }
        }
    }

    val isPermissionGranted: Boolean
        get() = devicePermissionsState.all { it.value.permissionGranted }


    val devices: List<UsbDevice>
        get() = devicePermissionsState.map { it.key }
    /**
     * Finds all via USB connected devices and requests for each USB device the permission to
     * communicate with it. If the user grants the permissions for all devices, the [callback] is
     * called with `true`. Otherwise `false`.
     */
    fun requestUsbPermissions(callback: ((success: Boolean) -> Unit)?) {
        this.callback = callback
        devicePermissionsState.clear()
        gatherUsbDevices()
        requestPermission()
    }

    private fun gatherUsbDevices() {
        val iterator = usbManager.deviceList.iterator()
        while (iterator.hasNext()) {
            val usbDevice = iterator.next().value
            devicePermissionsState[usbDevice] = PermissionState(
                usbManager.hasPermission(usbDevice)
            )
        }
    }

    private fun requestPermission() {
        synchronized(this) {
            val usbDevicesWithoutPermission = devicePermissionsState.filter { entry ->
                !entry.value.permissionRequested && !entry.value.permissionGranted
            }

            if (devicePermissionsState.isEmpty()) { // No USB devices found
                callback?.invoke(false)
                unregisterReceiver()
                return
            } else if (usbDevicesWithoutPermission.isEmpty()) {
                // We have either already asked for permission for the device or it was already
                // granted
                if (devicePermissionsState.any { !it.value.permissionGranted }) {
                    callback?.invoke(false) // User has denied at least one permission
                } else {
                    callback?.invoke(true) // User has granted permissions for all devices
                }
                unregisterReceiver()
                return
            }

            val usbDevice = usbDevicesWithoutPermission.toList().first().first

            val intent = Intent(ACTION_USB_PERMISSION).apply { `package` = context.packageName }
            val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, FLAG_MUTABLE)
            val filter = IntentFilter(ACTION_USB_PERMISSION)

            context.registerNotExportedReceiver(usbPermissionReceiver, filter)
            usbManager.requestPermission(usbDevice, pendingIntent)
        }
    }

    private fun unregisterReceiver() {
        try {
            context.unregisterReceiver(usbPermissionReceiver)
        } catch (exception: IllegalArgumentException) {
            Log.w("UsbPermissions",
                "Tried to unregister USB Permission receiver that was not registered.",
                exception
            )
        }
    }
}