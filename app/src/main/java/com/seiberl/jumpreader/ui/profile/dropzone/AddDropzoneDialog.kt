package com.seiberl.jumpreader.ui.profile.dropzone

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
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import com.seiberl.jumpreader.R
import com.seiberl.jumpreader.persistance.entities.Dropzone

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddDropzoneDialog(onConfirm: (dropzone: Dropzone) -> Unit, onDismiss: () -> Unit) {
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
                    text = stringResource(R.string.profile_dropzone_dialog_title),
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                var dropzone by remember { mutableStateOf("") }
                var location by remember { mutableStateOf("") }
                var icao by remember { mutableStateOf("") }
                val (checkedState, onStateChange) = remember { mutableStateOf(true) }

                OutlinedTextField(
                    value = dropzone,
                    label = { Text("Dropzone") },
                    textStyle = LocalTextStyle.current,
                    leadingIcon = { Icon(painterResource(R.drawable.title), null) },
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next
                    ),
                    maxLines = 1,
                    onValueChange = { newValue -> dropzone = newValue },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                )

                OutlinedTextField(
                    value = location,
                    label = { Text("Location") },
                    textStyle = LocalTextStyle.current,
                    leadingIcon = { Icon(painterResource(R.drawable.country), null) },
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next
                    ),
                    maxLines = 1,
                    onValueChange = { newValue -> location = newValue },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                )

                OutlinedTextField(
                    value = icao,
                    label = { Text("ICAO-Flugplatzcode") },
                    textStyle = LocalTextStyle.current,
                    leadingIcon = { Icon(painterResource(R.drawable.flight_takeoff), null) },
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Characters,
                        imeAction = ImeAction.Done
                    ),
                    maxLines = 1,
                    onValueChange = { newValue -> icao = newValue },
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
                                Dropzone(
                                    name = dropzone,
                                    location = location,
                                    icao = icao,
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