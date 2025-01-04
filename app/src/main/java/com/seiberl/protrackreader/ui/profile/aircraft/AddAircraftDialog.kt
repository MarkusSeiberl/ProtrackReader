package com.seiberl.protrackreader.ui.profile.aircraft

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.seiberl.protrackreader.R
import com.seiberl.protrackreader.persistance.entities.Aircraft

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddAircraftDialog(onConfirm: (aircraft: Aircraft) -> Unit, onDismiss: () -> Unit) {
    BasicAlertDialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .wrapContentWidth()
                .wrapContentHeight(),
            shape = MaterialTheme.shapes.large,
            tonalElevation = AlertDialogDefaults.TonalElevation
        ) {
            val verticalScrollState = rememberScrollState()

            Column(
                modifier = Modifier.padding(12.dp).verticalScroll(verticalScrollState)
            ) {
                Icon(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(16.dp)
                        .size(24.dp),
                    painter = painterResource(id = R.drawable.list_add),
                    contentDescription = null
                )

                Text(
                    text = "Add New Aircraft",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                var aircraftName by remember { mutableStateOf("") }
                var aircraftRegistration by remember { mutableStateOf("") }
                val (checkedState, onStateChange) = remember { mutableStateOf(true) }

                OutlinedTextField(
                    value = aircraftName,
                    label = { Text("Aircraft Name") },
                    textStyle = LocalTextStyle.current,
                    leadingIcon = { Icon(painterResource(R.drawable.aircraft), null) },
                    maxLines = 1,
                    onValueChange = { newValue -> aircraftName = newValue },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                )

                OutlinedTextField(
                    value = aircraftRegistration,
                    label = { Text("Registration") },
                    textStyle = LocalTextStyle.current,
                    leadingIcon = { Icon(painterResource(R.drawable.pin), null) },
                    maxLines = 1,
                    onValueChange = { newValue -> aircraftRegistration = newValue },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                )

                Row(
                    Modifier.fillMaxWidth()
                        .height(56.dp)
                        .toggleable(
                            value = checkedState,
                            onValueChange = { onStateChange(!checkedState) },
                            role = Role.Checkbox
                        )
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = checkedState,
                        onCheckedChange = null
                    )
                    Text(
                        text = "Set as Favorite",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Row(modifier = Modifier.align(Alignment.End)) {
                    TextButton(
                        onClick = onDismiss,
                    ) {
                        Text(stringResource(R.string.permission_dialog_cancel))
                    }

                    TextButton(
                        onClick = {
                            onConfirm(
                                Aircraft(
                                    name = aircraftName,
                                    registration = aircraftRegistration,
                                    favorite = checkedState
                                )
                            )
                        },
                    ) {
                        Text(stringResource(R.string.button_action_proceed))
                    }
                }
            }
        }
    }
}