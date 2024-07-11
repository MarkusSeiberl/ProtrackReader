package com.seiberl.protrackreader.ui.jumpimport

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import com.seiberl.protrackreader.MainActivity
import com.seiberl.protrackreader.ui.jumpimport.screens.ImportScreen
import com.seiberl.protrackreader.ui.theme.ProtrackReaderTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class JumpImportActivity : ComponentActivity() {

    private val viewModel: JumpImportViewModel by viewModels()

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.onShowNextActivity = ::onShowNextActivity

        setContent {
            ProtrackReaderTheme {
                val windowSize = calculateWindowSizeClass(activity = this)
                ImportScreen(viewModel, windowSize.widthSizeClass)
            }
        }
    }

    private fun onShowNextActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}