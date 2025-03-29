package com.seiberl.jumpreader.ui.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.seiberl.jumpreader.R
import com.seiberl.jumpreader.persistance.entities.Aircraft
import com.seiberl.jumpreader.persistance.entities.Canopy
import com.seiberl.jumpreader.persistance.entities.Dropzone
import com.seiberl.jumpreader.persistance.views.JumpMetaData

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditMetaDataDialog(
    jumps: List<JumpMetaData>,
    aircraft: List<Aircraft>,
    canopies: List<Canopy>,
    dropzones: List<Dropzone>,
    onEditMetaData: (List<JumpMetaData>, Aircraft?, Canopy?, Dropzone?) -> Unit,
    onDismiss: () -> Unit
) {

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
                modifier = Modifier
                    .padding(12.dp)
                    .verticalScroll(verticalScrollState)
            ) {
                Icon(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(16.dp)
                        .size(24.dp),
                    painter = painterResource(id = R.drawable.edit_note),
                    contentDescription = null
                )

                Text(
                    text = stringResource(R.string.edit_meta_dialog_title),
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                Text(
                    text = stringResource(R.string.jumplist_edit_meta_description),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(8.dp)
                )

                val firstSelectedAircraft =
                    if (jumps.isEmpty() || jumps.size > 1 || aircraft.isEmpty())
                        ""
                    else
                        aircraft.find { it.id == jumps[0].aircraftId }?.name ?: ""

                var expandedAircraft by remember { mutableStateOf(false) }
                var selectedAircraft by remember { mutableStateOf(firstSelectedAircraft) }

                ExposedDropdownMenuBox(
                    expanded = expandedAircraft,
                    onExpandedChange = { expandedAircraft = it },
                ) {
                    TextField(
                        modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable).padding(16.dp),
                        value = selectedAircraft,
                        onValueChange = {},
                        readOnly = true,
                        singleLine = true,
                        label = { Text(stringResource(R.string.edit_meta_dialog_choose_aircraft)) },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedAircraft) },
                        colors = ExposedDropdownMenuDefaults.textFieldColors(),
                    )
                    ExposedDropdownMenu(
                        expanded = expandedAircraft,
                        onDismissRequest = {
                            expandedAircraft = false
                        },
                    ) {
                        DropdownMenuItem(
                            text = { Text("-") },
                            onClick = {
                                selectedAircraft = ""
                                expandedAircraft = false
                            },
                            contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                        )
                        aircraft.forEach { option ->
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        option.name,
                                        style = MaterialTheme.typography.bodyLarge
                                    )
                                },
                                onClick = {
                                    selectedAircraft = option.name
                                    expandedAircraft = false
                                },
                                contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                            )
                        }
                    }
                }

                val firstSelectedCanopy =
                    if (jumps.isEmpty() || jumps.size > 1 || canopies.isEmpty()) ""
                    else canopies.find { it.id == jumps[0].canopyId }?.name ?: ""

                var expandedCanopy by remember { mutableStateOf(false) }
                var selectedCanopy by remember { mutableStateOf(firstSelectedCanopy) }

                ExposedDropdownMenuBox(
                    expanded = expandedCanopy,
                    onExpandedChange = { expandedCanopy = it },
                ) {
                    TextField(
                        modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable).padding(16.dp),
                        value = selectedCanopy,
                        onValueChange = {},
                        readOnly = true,
                        singleLine = true,
                        label = { Text(stringResource(R.string.edit_meta_dialog_choose_canopy)) },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedCanopy) },
                        colors = ExposedDropdownMenuDefaults.textFieldColors(),
                    )
                    ExposedDropdownMenu(
                        expanded = expandedCanopy,
                        onDismissRequest = {
                            expandedCanopy = false
                        },
                    ) {
                        DropdownMenuItem(
                            text = { Text("-") },
                            onClick = {
                                selectedCanopy = ""
                                expandedCanopy = false
                            },
                            contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                        )
                        canopies.forEach { option ->
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        option.name,
                                        style = MaterialTheme.typography.bodyLarge
                                    )
                                },
                                onClick = {
                                    selectedCanopy = option.name
                                    expandedCanopy = false
                                },
                                contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                            )
                        }
                    }
                }

                val firstSelectedDropzone =
                    if (jumps.isEmpty() || jumps.size > 1 || dropzones.isEmpty()) ""
                    else dropzones.find { it.id == jumps[0].dropzoneId }?.name ?: ""

                var expandedDropzone by remember { mutableStateOf(false) }
                var selectedDropzone by remember { mutableStateOf(firstSelectedDropzone) }

                ExposedDropdownMenuBox(
                    expanded = expandedDropzone,
                    onExpandedChange = { expandedDropzone = it },
                ) {
                    TextField(
                        modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable).padding(16.dp),
                        value = selectedDropzone,
                        onValueChange = {},
                        readOnly = true,
                        singleLine = true,
                        label = { Text(stringResource(R.string.edit_meta_dialog_choose_dropzone)) },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedDropzone) },
                        colors = ExposedDropdownMenuDefaults.textFieldColors(),
                    )
                    ExposedDropdownMenu(
                        expanded = expandedDropzone,
                        onDismissRequest = {
                            expandedDropzone = false
                        },
                    ) {
                        DropdownMenuItem(
                            text = { Text("-") },
                            onClick = {
                                selectedDropzone = ""
                                expandedDropzone = false
                            },
                            contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                        )
                        dropzones.forEach { option ->
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        option.name,
                                        style = MaterialTheme.typography.bodyLarge
                                    )
                                },
                                onClick = {
                                    selectedDropzone = option.name
                                    expandedDropzone = false
                                },
                                contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                            )
                        }
                    }
                }


                Row(modifier = Modifier.align(Alignment.End)) {
                    TextButton(
                        onClick = onDismiss,
                    ) {
                        Text(stringResource(R.string.permission_dialog_cancel))
                    }

                    TextButton(
                        onClick = {
                            onEditMetaData(
                                jumps,
                                aircraft.find { it.name == selectedAircraft },
                                canopies.find { it.name == selectedCanopy },
                                dropzones.find { it.name == selectedDropzone })
                        },
                    ) {
                        Text(stringResource(R.string.edit_meta_dialog_update_action))
                    }
                }
            }
        }
    }
}