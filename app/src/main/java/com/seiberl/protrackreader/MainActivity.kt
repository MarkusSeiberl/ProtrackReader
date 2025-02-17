package com.seiberl.protrackreader

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.seiberl.protrackreader.ui.Navigation
import com.seiberl.protrackreader.ui.home.JumpListViewModel
import com.seiberl.protrackreader.ui.jumpimport.JumpImportActivity
import com.seiberl.protrackreader.ui.theme.ProtrackReaderTheme
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "MainActivity"
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

        jumpListViewModel.fabClickEvent = ::onShowJumpImportView
        jumpListViewModel.onRequestPermission = ::onRequestPermission
        jumpListViewModel.onShareFile = ::onShareFile
    }

    override fun onResume() {
        super.onResume()
        if (jumpListViewModel.shouldRestartApp) {
            restartApp()
        }
    }

    private fun onRequestPermission() {
        Log.d(TAG, "Requesting storage permission.")
        val uri = Uri.parse("package:${BuildConfig.APPLICATION_ID}")
        val intent = Intent(ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION, uri)
        startActivity(intent)
    }

    private fun onShowJumpImportView() {
        val intent = Intent(this, JumpImportActivity::class.java)
        startActivity(intent)
    }

    private fun onShareFile(fileUri: Uri) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        intent.setType("application/pdf")
        intent.putExtra(Intent.EXTRA_STREAM, fileUri)


        startActivity(Intent.createChooser(intent, null))
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
