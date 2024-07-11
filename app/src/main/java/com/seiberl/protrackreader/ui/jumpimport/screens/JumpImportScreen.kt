package com.seiberl.protrackreader.ui.jumpimport.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.seiberl.protrackreader.ui.jumpimport.ImportUiState
import com.seiberl.protrackreader.ui.jumpimport.JumpImportViewModel
import com.seiberl.protrackreader.ui.jumpimport.models.ImportState

@Composable
fun ImportScreen(viewModel: JumpImportViewModel, windowsWidth: WindowWidthSizeClass) {

    val uiState: ImportUiState by viewModel.uiState.collectAsStateWithLifecycle()

    val blur = if (uiState.importState == ImportState.IMPORT_ONGOING) 8.dp else 0.dp



    when (windowsWidth) {
        WindowWidthSizeClass.Expanded -> Row(
            modifier = Modifier
                .fillMaxSize()
                .blur(blur),
            horizontalArrangement = Arrangement.Absolute.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            ImageSection(
                Modifier
                    .weight(0.4f)
                    .padding(32.dp)
            )
            DescriptionSection(
                Modifier
                    .weight(0.6f)
                    .padding(start = 32.dp, top = 32.dp, end = 32.dp)
                    .verticalScroll(rememberScrollState()),
                viewModel,
                windowsWidth
            )
        }

        WindowWidthSizeClass.Compact, WindowWidthSizeClass.Medium -> Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp)
                .blur(blur),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            ImageSection(
                Modifier
                    .weight(0.4f)
                    .padding(top = 64.dp)
            )
            DescriptionSection(
                Modifier
                    .weight(0.6f)
                    .padding(horizontal = 32.dp)
                    .verticalScroll(rememberScrollState()),
                viewModel,
                windowsWidth
            )
        }
    }

    if (uiState.importState == ImportState.IMPORT_ONGOING) {
        ImportOverlay(viewModel)
    }
}