package com.seiberl.protrackreader.ui.home

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
import androidx.compose.foundation.text.KeyboardActions
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
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.seiberl.protrackreader.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrintJumpsDialog(
    minJumpNr: Int,
    maxJumpNr: Int,
    fromJumpFieldError: Boolean,
    toJumpFieldError: Boolean,
    onCreatePdfOfJumps: (String, String) -> Unit,
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
                modifier = Modifier.padding(12.dp).verticalScroll(verticalScrollState)
            ) {
                Icon(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(16.dp)
                        .size(24.dp),
                    painter = painterResource(id = R.drawable.pdf),
                    contentDescription = null
                )

                Text(
                    text = "Create PDF",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                Text(
                    text = "Enter the jump numbers you want to include in the PDF.",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.align(Alignment.CenterHorizontally).padding(8.dp)
                )

                var jumpNrFrom by remember { mutableStateOf("") }
                var jumpNrTo by remember { mutableStateOf("") }

                val supportedFromText: @Composable (() -> Unit)? = if (fromJumpFieldError) {
                    { Text("Enter jump number between $minJumpNr and $maxJumpNr", color = MaterialTheme.colorScheme.error) }
                } else {
                    null
                }
                val supportedToText: @Composable (() -> Unit)? = if (toJumpFieldError) {
                    { Text("Enter jump number between $minJumpNr and $maxJumpNr", color = MaterialTheme.colorScheme.error) }
                } else {
                    null
                }

                val focusManager = LocalFocusManager.current

                OutlinedTextField(
                    value = jumpNrFrom,
                    onValueChange = { newValue -> jumpNrFrom = newValue },
                    label = { Text("From") },
                    supportingText = supportedFromText,
                    textStyle = LocalTextStyle.current,
                    maxLines = 1,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = { focusManager.moveFocus(FocusDirection.Down) }
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                )

                OutlinedTextField(
                    value = jumpNrTo,
                    onValueChange = { newValue -> jumpNrTo = newValue },
                    label = { Text("To") },
                    supportingText = supportedToText,
                    textStyle = LocalTextStyle.current,
                    maxLines = 1,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            focusManager.clearFocus()
                            onCreatePdfOfJumps(
                                jumpNrFrom, jumpNrTo
                            )
                        }
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                )

                Spacer(modifier = Modifier.height(24.dp))

                Row(modifier = Modifier.align(Alignment.End)) {
                    TextButton(
                        onClick = onDismiss,
                    ) {
                        Text(stringResource(R.string.permission_dialog_cancel))
                    }

                    TextButton(
                        onClick = {
                            onCreatePdfOfJumps(
                                jumpNrFrom, jumpNrTo
                            )
                        },
                    ) {
                        Text("Create PDF")
                    }
                }
            }
        }
    }
}