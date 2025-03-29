package com.seiberl.jumpreader.ui.jumpimport

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import com.seiberl.jumpreader.BuildConfig
import com.seiberl.jumpreader.MainActivity
import com.seiberl.jumpreader.ui.jumpimport.screens.ImportScreen
import com.seiberl.jumpreader.ui.theme.ProtrackReaderTheme
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "JumpImportActivity"

@AndroidEntryPoint
class JumpImportActivity : ComponentActivity() {

    private val viewModel: JumpImportViewModel by viewModels()

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.onShowNextActivity = ::onShowNextActivity
        viewModel.onRequestPermission = ::onRequestPermission
        viewModel.onRestartApp = ::restartApp

        setContent {
            ProtrackReaderTheme {
                val windowSize = calculateWindowSizeClass(activity = this)
                ImportScreen(viewModel, windowSize.widthSizeClass)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.updateStoragePermission()
    }

    private fun onRequestPermission() {
        Log.d(TAG, "Requesting storage permission.")
        val uri = Uri.parse("package:${BuildConfig.APPLICATION_ID}")
        val intent = Intent(ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION, uri)
        startActivity(intent)
    }

    private fun onShowNextActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun restartApp() {
        val intent = packageManager.getLaunchIntentForPackage(packageName)
        if (intent != null) {
            Log.d(TAG, "User granted permission. Restarting App for permissions to take effect.")
            val mainIntent = Intent.makeRestartActivityTask(intent.component)
            startActivity(mainIntent)
            Runtime.getRuntime().exit(0)
        }
    }
}