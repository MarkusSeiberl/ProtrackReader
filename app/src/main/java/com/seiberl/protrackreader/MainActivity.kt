package com.seiberl.protrackreader

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.seiberl.protrackreader.ui.Navigation
import com.seiberl.protrackreader.ui.home.JumpListViewModel
import com.seiberl.protrackreader.ui.jumpimport.JumpImportActivity
import com.seiberl.protrackreader.ui.theme.ProtrackReaderTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val jumpListViewModel: JumpListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ProtrackReaderTheme {
                Navigation(jumpListViewModel)
            }
        }

        if (!Environment.isExternalStorageManager()) {
            val uri = Uri.parse("package:${BuildConfig.APPLICATION_ID}")
            val intent = Intent(ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION, uri)
            startActivity(intent)
        }

        jumpListViewModel.clickEvent = ::onShowJumpImportView
    }

    private fun onShowJumpImportView() {
        val intent = Intent(this, JumpImportActivity::class.java)
        startActivity(intent)
    }
}
