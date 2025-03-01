package com.seiberl.protrackreader.ui.jumpimport.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.seiberl.protrackreader.R
import com.seiberl.protrackreader.ui.jumpdetail.InfoBox
import com.seiberl.protrackreader.ui.jumpimport.JumpImportViewModel
import com.seiberl.protrackreader.ui.jumpimport.models.ImportState.IMPORT_START_FAILED
import com.seiberl.protrackreader.ui.profile.Favorites

@Composable
fun DescriptionSection(
    modifier: Modifier = Modifier,
    viewModel: JumpImportViewModel,
    windowWidth: WindowWidthSizeClass,
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        val titleStyle =
            if (windowWidth == WindowWidthSizeClass.Compact) {
                MaterialTheme.typography.headlineMedium
            } else {
                MaterialTheme.typography.headlineLarge
            }

        Text(
            text = stringResource(id = R.string.jumpimport_title),
            style = titleStyle,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.size(32.dp))

        Text(
            text = stringResource(R.string.jumpimport_body_description),
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Justify
        )
        Text(
            text = stringResource(R.string.jumpimport_favorites_import),
            style = MaterialTheme.typography.bodySmall
        )

        Spacer(modifier = Modifier.size(32.dp))

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = viewModel::onImportClick
            ) {
                Text(
                    text = stringResource(R.string.jumpimport_button_import),
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.size(8.dp))

            OutlinedButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = viewModel::onCancelClick
            ) {
                Text(
                    text = stringResource(R.string.jumpimport_button_cancel),
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center
                )
            }

            if (uiState.importState == IMPORT_START_FAILED) {
                Spacer(modifier = Modifier.size(8.dp))
                Text(
                    text = stringResource(R.string.jumpimport_volume_not_found),
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}