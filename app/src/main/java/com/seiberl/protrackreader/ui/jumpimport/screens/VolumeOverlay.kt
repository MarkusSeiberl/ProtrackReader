package com.seiberl.protrackreader.ui.jumpimport.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import com.seiberl.protrackreader.R
import com.seiberl.protrackreader.ui.jumpimport.ImportUiState
import com.seiberl.protrackreader.ui.jumpimport.JumpImportViewModel
import com.seiberl.protrackreader.ui.jumpimport.models.ImportState
import com.seiberl.protrackreader.ui.jumpimport.models.ImportState.SEARCHING_VOLUME
import com.seiberl.protrackreader.ui.jumpimport.models.ImportState.SEARCHING_VOLUME_FAILED
import com.seiberl.protrackreader.ui.jumpimport.models.ImportState.VOLUME_EMPTY

@Composable
fun VolumeOverlay(viewModel: JumpImportViewModel) {

    val uiState: ImportUiState by viewModel.uiState.collectAsStateWithLifecycle()

    Box(modifier = Modifier
        .alpha(0.9f)
        .background(MaterialTheme.colorScheme.background)
        .padding(24.dp, 0.dp)
        .fillMaxSize()
    ) {


        if (uiState.importState == SEARCHING_VOLUME) {
            SearchingVolumeOverlay(
                modifier = Modifier.align(Alignment.Center),
                viewModel = viewModel
            )
        } else if (uiState.importState == SEARCHING_VOLUME_FAILED) {
            SearchVolumeFailedOverlay(
                modifier = Modifier.align(Alignment.Center),
                viewModel = viewModel
            )
        } else if (uiState.importState == VOLUME_EMPTY) {
            VolumeEmptyOverlay(
                modifier = Modifier.align(Alignment.Center),
                viewModel = viewModel
            )
        }
    }
}


@Composable
fun SearchingVolumeOverlay(
    modifier: Modifier = Modifier,
    viewModel: JumpImportViewModel
) {

    Column(modifier = modifier) {

        CircularProgressIndicator(
            modifier = Modifier
                .size(64.dp)
                .align(Alignment.CenterHorizontally),
            color = MaterialTheme.colorScheme.secondary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant,
        )

        Spacer(modifier = Modifier.size(24.dp))

        Text(text = stringResource(R.string.jumpimport_waiting_protrack))

        Spacer(modifier = Modifier.size(24.dp))

        Button(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            onClick = { viewModel.onAbortClicked() }
        ) {
            Text(
                text = stringResource(id = R.string.jumpimport_button_abort),
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun SearchVolumeFailedOverlay(
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

        Text(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            text = stringResource(R.string.jumpimport_protrack2_access_failed)
        )

        Spacer(modifier = Modifier.size(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                modifier = Modifier,
                onClick = { viewModel.onAbortClicked() }
            ) {
                Text(
                    text = stringResource(id = R.string.jumpimport_button_abort),
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center
                )
            }

            Button(
                modifier = Modifier,
                onClick = { viewModel.startVolumeDetection() }
            ) {
                Text(
                    text = stringResource(id = R.string.jumpimport_button_retry),
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
fun VolumeEmptyOverlay(
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

        Text(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            text = stringResource(R.string.jumpimport_protrack2_access_failed_restart)
        )

        Spacer(modifier = Modifier.size(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                modifier = Modifier,
                onClick = { viewModel.onAbortClicked() }
            ) {
                Text(
                    text = stringResource(id = R.string.jumpimport_button_abort),
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center
                )
            }

            Button(
                modifier = Modifier,
                onClick = { viewModel.restartApp() }
            ) {
                Text(
                    text = stringResource(id = R.string.jumpimport_button_restart),
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}