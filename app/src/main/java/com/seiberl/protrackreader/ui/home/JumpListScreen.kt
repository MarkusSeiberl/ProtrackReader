package com.seiberl.protrackreader.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.seiberl.protrackreader.R
import com.seiberl.protrackreader.persistance.views.JumpMetaData
import com.seiberl.protrackreader.ui.Screen
import com.seiberl.protrackreader.ui.theme.surfaceBrightLight
import java.text.SimpleDateFormat
import java.time.ZoneId
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JumpListScreen(
    viewModel: JumpListViewModel,
    navController: NavController,
) {

    val scrollBehavior =
        TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())

    val uiState: JumpListUiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        contentWindowInsets = ScaffoldDefaults.contentWindowInsets.exclude(WindowInsets.systemBars),
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            var showMenu by remember { mutableStateOf(false) }

            MediumTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceBright,
                    scrolledContainerColor = MaterialTheme.colorScheme.surfaceBright,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                scrollBehavior = scrollBehavior,
                title = {
                    Text(stringResource(R.string.jumplist_title))
                },
                actions = {
                    if (uiState.selectedJumps.isNotEmpty()) {
                        IconButton(
                            onClick = { viewModel.deleteSelectedJumps() }
                        ) {
                            Icon(
                                Icons.Filled.Delete,
                                contentDescription =
                                stringResource(id = R.string.jumplist_delete_icon_description)
                            )
                        }
                    }
                    IconButton(onClick = { showMenu = !showMenu }) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "More",
                        )
                        DropdownMenu(expanded = showMenu, onDismissRequest = { showMenu = false }) {
                            DropdownMenuItem(
                                text = { Text("Set Meta Data") },
                                onClick = { showMenu = false; TODO() }
                            )
                            if (uiState.jumps.isNotEmpty()) {
                                DropdownMenuItem(
                                    text = { Text("Create PDF") },
                                    onClick = {
                                        showMenu = false
                                        viewModel.onCreatePdfClicked()
                                    }
                                )
                            }
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier,
                onClick = { viewModel.onJumpImportClick() },
                containerColor = BottomAppBarDefaults.bottomAppBarFabColor,
                elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation()
            ) {
                Icon(Icons.Filled.Add, stringResource(id = R.string.jumpimport_title))
            }
        }
    ) { padding ->

        if (uiState.showPermissionDialog) {
            PermissionDialog(
                viewModel::onPermissionDialogConfirmed,
                viewModel::onPermissionDialogDismiss,
                viewModel::onPermissionDialogDismiss
            )
        }
        
        if (uiState.showPrintJumpsDialog) {
            val minJumpNr = uiState.jumps.lastOrNull()?.number ?: 0
            val maxJumpNr = uiState.jumps.firstOrNull()?.number ?: 0
            val fromJumpFieldError = uiState.dialogErrorFromJumpField
            val toJumpFieldError = uiState.dialogErrorToJumpField
            PrintJumpsDialog(
                minJumpNr,
                maxJumpNr,
                fromJumpFieldError,
                toJumpFieldError,
                viewModel::printJumps,
                viewModel::onPrintJumpsDialogDismiss
            )
        }

        if (uiState.jumps.isEmpty()) {
            EmptyListContent(padding = padding)
        } else {
            ScrollContent(padding = padding, viewModel, navController = navController)
        }

    }
}

@Composable
fun EmptyListContent(padding: PaddingValues) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surfaceBright)
            .padding(padding)
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Connect your phone to a ProTrack device to import jumps.")
    }
}


@Composable
fun ScrollContent(
    padding: PaddingValues,
    viewModel: JumpListViewModel,
    navController: NavController
) {

    val uiState: JumpListUiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surfaceBright)
            .padding(padding),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LazyColumn {
            items(uiState.jumps, key = {jump -> jump.number}) { jump ->

                if (jump.isInDifferentMonth(uiState.jumps)) {
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp, 16.dp, 16.dp, 8.dp),
                        horizontalArrangement = Arrangement.End
                    ) {
                        val formatter = SimpleDateFormat("MMMM yyyy", Locale.getDefault())
                        val formattedDate = formatter.format(Date.from(jump.timestamp))
                        Text(formattedDate)
                    }
                }

                Box(modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 4.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .clickable {
                        navController.navigate(Screen.JumpDetailScreen(jump.number))
                    }
                ) {
                    val isSelected = uiState.selectedJumps.contains(jump.number)
                    JumpItem(jump, isSelected, viewModel::onJumpSelected)
                }
            }
        }
    }
}

private fun JumpMetaData.isInDifferentMonth(jumps: List<JumpMetaData>): Boolean {
    val nextJump = jumps.filter { this.number < it.number }.minByOrNull { it.timestamp }
    if (nextJump == null) return true

    val currentJumpMonth = timestamp.atZone(ZoneId.systemDefault()).monthValue
    val currentJumpYear = timestamp.atZone(ZoneId.systemDefault()).year

    val previousJumpMonth = nextJump.timestamp.atZone(ZoneId.systemDefault()).monthValue
    val previousJumpJumpYear = nextJump.timestamp.atZone(ZoneId.systemDefault()).year

    return previousJumpJumpYear != currentJumpYear || previousJumpMonth != currentJumpMonth
}
