package com.seiberl.protrackreader

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.storage.StorageManager
import android.provider.Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION
import android.provider.Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.findNavController
import androidx.navigation.toRoute
import com.seiberl.protrackreader.ui.Navigation
import com.seiberl.protrackreader.ui.jumpimport.permissions.UsbPermissions
import com.seiberl.protrackreader.ui.theme.ProtrackReaderTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.serialization.Serializable

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ProtrackReaderTheme {
                Navigation()
            }
        }

        if (intent.action == "android.hardware.usb.action.USB_DEVICE_ATTACHED") {
            //findNavController(R.id.nav_host_fragment_content_main).navigate(JumpImportScreen)
        }

        val storageManager = getSystemService(Context.STORAGE_SERVICE) as StorageManager
        val volumes = storageManager.storageVolumes
        val protrackVolume = volumes.find { it.getDescription(this).contains("PROTRACK2") }

        if (!Environment.isExternalStorageManager()) {
            val uri = Uri.parse("package:${BuildConfig.APPLICATION_ID}")
            val intent = Intent(ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION, uri)
            startActivity(intent)
        }

        val dir = protrackVolume?.directory?.listFiles().contentToString()

//        val permissions = UsbPermissions(this)
//        permissions.requestUsbPermissions() {
//            if (it) {
//                val devices = permissions.devices
//                Log.d("TAAG", "sdf ${devices.size}")
//            }
//        }

        // usbdevice.devicename = /dev/bus/usb/001/006

    }
}
