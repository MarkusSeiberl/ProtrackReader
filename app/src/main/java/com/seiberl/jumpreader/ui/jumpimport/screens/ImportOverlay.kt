package com.seiberl.jumpreader.ui.jumpimport.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.seiberl.jumpreader.R
import com.seiberl.jumpreader.ui.jumpimport.ImportUiState
import com.seiberl.jumpreader.ui.jumpimport.JumpImportViewModel
import com.seiberl.jumpreader.ui.jumpimport.models.ImportState

@Composable
fun ImportOverlay(viewModel: JumpImportViewModel) {

    val uiState: ImportUiState by viewModel.uiState.collectAsStateWithLifecycle()

    Box(modifier = Modifier
        .alpha(0.9f)
        .background(MaterialTheme.colorScheme.background)
        .fillMaxSize()
    ) {


        if (uiState.importState == ImportState.IMPORT_ONGOING) {
            ImportOnGoingOverlay(
                modifier = Modifier.align(Alignment.Center),
                viewModel = viewModel
            )
        } else if (uiState.importState == ImportState.IMPORT_SUCCESSFUL) {
            ImportSuccessfulOverlay(
                modifier = Modifier.align(Alignment.Center),
                viewModel = viewModel
            )
        } else if (uiState.importState == ImportState.IMPORT_FAILED) {
            ImportFailedOverlay(
                modifier = Modifier.align(Alignment.Center),
                viewModel = viewModel
            )
        }


    }
}

@Composable
fun ImportOnGoingOverlay(
    modifier: Modifier = Modifier,
    viewModel: JumpImportViewModel
) {

    val uiState: ImportUiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(modifier = modifier) {

        CircularProgressIndicator(
            modifier = Modifier
                .size(64.dp)
                .align(Alignment.CenterHorizontally),
            color = MaterialTheme.colorScheme.secondary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant,
        )

        Spacer(modifier = Modifier.size(24.dp))

        Text(text = stringResource(R.string.jumpimport_importing_jumps, uiState.currentJumpNumber))
    }
}

@Composable
fun ImportSuccessfulOverlay(
    modifier: Modifier = Modifier,
    viewModel: JumpImportViewModel
) {

    val uiState: ImportUiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(modifier = modifier) {

        Icon(
            modifier = Modifier
                .size(64.dp)
                .align(Alignment.CenterHorizontally),
            painter = painterResource(id = R.drawable.check_circle),
            tint = MaterialTheme.colorScheme.tertiary,
            contentDescription = null
        )

        Spacer(modifier = Modifier.size(24.dp))

        Text(text = stringResource(R.string.jumpimport_imported_jumps, uiState.importedJumps))

        Spacer(modifier = Modifier.size(24.dp))

        Button(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            onClick = { viewModel.onContinueClick() }
        ) {
            Text(
                text = stringResource(id = R.string.button_action_continue),
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
            )
        }
    }
}


@Composable
fun ImportFailedOverlay(
    modifier: Modifier = Modifier,
    viewModel: JumpImportViewModel
) {
    Column(modifier = modifier) {

        Icon(
            modifier = Modifier
                .size(64.dp)
                .align(Alignment.CenterHorizontally),
            painter = painterResource(id = R.drawable.error),
            tint = MaterialTheme.colorScheme.error,
            contentDescription = null
        )

        Spacer(modifier = Modifier.size(24.dp))

        Text(text = stringResource(R.string.jumpimport_import_failed), color = MaterialTheme.colorScheme.error)

        Spacer(modifier = Modifier.size(24.dp))

        Button(
            onClick = { viewModel.onImportClick() }
        ) {
            Text(
                text = stringResource(id = R.string.jumpimport_button_retry),
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
            )
        }
    }
}