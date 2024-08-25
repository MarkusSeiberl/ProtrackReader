package com.seiberl.protrackreader.ui.jumpimport

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
import com.seiberl.protrackreader.BuildConfig
import com.seiberl.protrackreader.MainActivity
import com.seiberl.protrackreader.ui.jumpimport.screens.ImportScreen
import com.seiberl.protrackreader.ui.theme.ProtrackReaderTheme
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

        setContent {
            ProtrackReaderTheme {
                val windowSize = calculateWindowSizeClass(activity = this)
                ImportScreen(viewModel, windowSize.widthSizeClass)
            }
        }
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
}