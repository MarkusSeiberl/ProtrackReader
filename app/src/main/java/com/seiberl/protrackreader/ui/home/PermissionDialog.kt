package com.seiberl.protrackreader.ui.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.seiberl.protrackreader.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PermissionDialog(onConfirm: () -> Unit, onDeny: () -> Unit, onDismiss: () -> Unit) {

    BasicAlertDialog(
        onDismissRequest = onDismiss,
    ) {
        Surface(
            modifier = Modifier
                .wrapContentWidth()
                .wrapContentHeight(),
            shape = MaterialTheme.shapes.large,
            tonalElevation = AlertDialogDefaults.TonalElevation
        ) {
            Column(modifier = Modifier.padding(24.dp)) {

                Icon(
                    modifier =
                    Modifier
                        .align(Alignment.CenterHorizontally)
                        .size(24.dp),
                    painter = painterResource(id = R.drawable.folder_supervised),
                    contentDescription = null
                )

                Text(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(0.dp, 16.dp),
                    text = stringResource(R.string.permission_dialog_title),
                    style = MaterialTheme.typography.titleLarge
                )

                Text(
                    text = stringResource(R.string.permission_dialog_description),
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(24.dp))


                Row(modifier = Modifier.align(Alignment.End)) {
                    TextButton(
                        onClick = onDeny,
                    ) {
                        Text(stringResource(R.string.permission_dialog_cancel))
                    }

                    TextButton(
                        onClick = onConfirm,
                    ) {
                        Text(stringResource(R.string.button_action_proceed))
                    }
                }
            }
        }
    }
}