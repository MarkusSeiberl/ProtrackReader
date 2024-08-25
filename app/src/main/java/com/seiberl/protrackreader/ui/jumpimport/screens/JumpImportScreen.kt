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
import com.seiberl.protrackreader.ui.home.PermissionDialog
import com.seiberl.protrackreader.ui.jumpimport.ImportUiState
import com.seiberl.protrackreader.ui.jumpimport.JumpImportViewModel
import com.seiberl.protrackreader.ui.jumpimport.models.ImportState.IMPORT_FAILED
import com.seiberl.protrackreader.ui.jumpimport.models.ImportState.IMPORT_ONGOING
import com.seiberl.protrackreader.ui.jumpimport.models.ImportState.IMPORT_SUCCESSFUL
import com.seiberl.protrackreader.ui.jumpimport.models.ImportState.PERMISSION_DENIED
import com.seiberl.protrackreader.ui.jumpimport.models.ImportState.PERMISSION_REQUIRED
import com.seiberl.protrackreader.ui.jumpimport.models.ImportState.SEARCHING_VOLUME
import com.seiberl.protrackreader.ui.jumpimport.models.ImportState.SEARCHING_VOLUME_FAILED
import com.seiberl.protrackreader.ui.jumpimport.models.ImportState.VOLUME_EMPTY

@Composable
fun ImportScreen(viewModel: JumpImportViewModel, windowsWidth: WindowWidthSizeClass) {

    val uiState: ImportUiState by viewModel.uiState.collectAsStateWithLifecycle()

    val shouldBlurContent = listOf(
        PERMISSION_DENIED,
        SEARCHING_VOLUME,
        SEARCHING_VOLUME_FAILED,
        VOLUME_EMPTY,
        IMPORT_ONGOING,
        IMPORT_SUCCESSFUL,
        IMPORT_FAILED
    ).contains(uiState.importState)

    val blur = if (shouldBlurContent) 8.dp else 0.dp

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

    if (uiState.importState == PERMISSION_REQUIRED) {
        PermissionDialog(
            viewModel::onPermissionDialogConfirmed,
            viewModel::onPermissionDialogDismiss,
            viewModel::onPermissionDialogDismiss
        )
    }

    val shouldShowImportOverlay = listOf(
        IMPORT_ONGOING,
        IMPORT_SUCCESSFUL,
        IMPORT_FAILED
    ).contains(uiState.importState)

    val shouldShowVolumeOverlay = listOf(
        SEARCHING_VOLUME,
        SEARCHING_VOLUME_FAILED,
        VOLUME_EMPTY,
    ).contains(uiState.importState)

    if (shouldShowImportOverlay) {
        ImportOverlay(viewModel)
    } else if (shouldShowVolumeOverlay) {
        VolumeOverlay(viewModel)
    } else if (uiState.importState == PERMISSION_DENIED) {
        PermissionDeniedOverlay(viewModel)
    }
}