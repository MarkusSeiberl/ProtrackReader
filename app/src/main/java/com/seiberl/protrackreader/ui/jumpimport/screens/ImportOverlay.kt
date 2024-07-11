package com.seiberl.protrackreader.ui.jumpimport.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.seiberl.protrackreader.ui.jumpimport.ImportUiState
import com.seiberl.protrackreader.ui.jumpimport.JumpImportViewModel

@Composable
fun ImportOverlay(viewModel: JumpImportViewModel) {

    val uiState: ImportUiState by viewModel.uiState.collectAsStateWithLifecycle()

    Box(modifier = Modifier
        .alpha(0.9f)
        .background(MaterialTheme.colorScheme.background)
        .fillMaxSize()
    ) {
        Column(
            modifier = Modifier.align(Alignment.Center)
        ) {

            CircularProgressIndicator(
                modifier = Modifier
                    .size(64.dp)
                    .align(Alignment.CenterHorizontally),
                color = MaterialTheme.colorScheme.secondary,
                trackColor = MaterialTheme.colorScheme.surfaceVariant,
            )

            Spacer(modifier = Modifier.size(24.dp))

            Text(text = "Importing Jump: ${uiState.currentJumpNumber}")
        }
    }
}