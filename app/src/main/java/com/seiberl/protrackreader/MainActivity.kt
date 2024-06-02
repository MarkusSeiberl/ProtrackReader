package com.seiberl.protrackreader

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.seiberl.protrackreader.ui.Navigation
import com.seiberl.protrackreader.ui.theme.ProtrackReaderTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ProtrackReaderTheme {
                Navigation()
            }
        }

        if (!Environment.isExternalStorageManager()) {
            val uri = Uri.parse("package:${BuildConfig.APPLICATION_ID}")
            val intent = Intent(ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION, uri)
            startActivity(intent)
        }
    }
}
