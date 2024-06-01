package com.seiberl.protrackreader.ui.jumpimport

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass.Companion.Compact
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.seiberl.protrackreader.MainActivity
import com.seiberl.protrackreader.persistance.repository.JumpRepository
import com.seiberl.protrackreader.ui.jumpimport.screens.ImportScreen
import com.seiberl.protrackreader.ui.theme.ProtrackReaderTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class JumpImportActivity : ComponentActivity() {

    private val viewModel: JumpImportViewModel by viewModels()

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.onShowNextActivity = ::onShowNextActivity

        Log.d("TAAG", "test")

        enableEdgeToEdge()
        setContent {
            ProtrackReaderTheme {
                val windowSize = calculateWindowSizeClass(activity = this)
                ImportScreen(viewModel, windowSize.widthSizeClass)
            }
        }
        Log.d("TAAG", "test2")
    }

    private fun onShowNextActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}