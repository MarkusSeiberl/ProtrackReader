package com.seiberl.protrackreader.ui.jumpimport.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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

@Composable
fun PermissionDeniedOverlay(viewModel: JumpImportViewModel) {

    Box(modifier = Modifier
        .alpha(0.9f)
        .background(MaterialTheme.colorScheme.background)
        .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(24.dp, 0.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

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
                text = stringResource(R.string.jumpimport_permission_denied_explanation)
            )

            Spacer(modifier = Modifier.size(24.dp))

            Button(
                onClick = { viewModel.onAbortClicked()  }
            ) {
                Text(
                    text = stringResource(id = R.string.jumpimport_button_finish),
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}