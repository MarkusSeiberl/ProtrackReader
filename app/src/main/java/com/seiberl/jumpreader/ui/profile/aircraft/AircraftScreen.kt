package com.seiberl.jumpreader.ui.profile.aircraft

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue.EndToStart
import androidx.compose.material3.SwipeToDismissBoxValue.Settled
import androidx.compose.material3.SwipeToDismissBoxValue.StartToEnd
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.seiberl.jumpreader.R
import com.seiberl.jumpreader.persistance.entities.Aircraft
import com.seiberl.jumpreader.ui.profile.DismissBackground

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AircraftScreen(
    navController: NavController,
    viewModel: AircraftViewModel = hiltViewModel()
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text(text = stringResource(id = R.string.profile_aircraft_label))
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.button_action_back)
                        )
                    }
                },
                scrollBehavior = scrollBehavior,
            )
        },
        bottomBar = {
            Button(
                onClick = viewModel::onAddAircraftClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .systemBarsPadding()
                    .padding(16.dp)
            ) {
                Text(stringResource(R.string.profile_aircraft_add))
            }
        },
    ) { padding ->
        AircraftScrollContent(padding, viewModel)
    }
}

@Composable
fun AircraftScrollContent(padding: PaddingValues, viewModel: AircraftViewModel) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    if (uiState.showAddAircraftDialog) {
        AddAircraftDialog(
            onConfirm = { aircraft -> viewModel.addAircraft(aircraft) },
            onDismiss = { viewModel.onDialogDismiss() }
        )
    }

    Column(
        modifier = Modifier
            .padding(padding)
            .background(MaterialTheme.colorScheme.surface)
            .fillMaxSize()
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .clip(MaterialTheme.shapes.medium)
                .background(MaterialTheme.colorScheme.surfaceContainer),
        ) {
            itemsIndexed(uiState.aircraft) { index, aircraft ->
                AircraftItem(
                    aircraft,
                    onStarClicked = { favAircraft -> viewModel.starAircraft(favAircraft) },
                    onRemoveClicked = { favAircraft -> viewModel.removeAircraft(favAircraft) }
                )
                if (index < uiState.aircraft.lastIndex) {
                    HorizontalDivider(
                        modifier = Modifier
                            .padding(16.dp, 0.dp, 16.dp, 0.dp),
                        thickness = 1.dp,
                        color = MaterialTheme.colorScheme.outline
                    )
                }
            }
        }
    }
}

@Composable
fun AircraftItem(aircraft: Aircraft, onStarClicked: (Aircraft) -> Unit, onRemoveClicked: (Aircraft) -> Unit) {

    val dismissState = rememberSwipeToDismissBoxState()

    when(dismissState.currentValue) {
        EndToStart -> {
            LaunchedEffect(dismissState) {
                onRemoveClicked(aircraft)
                dismissState.snapTo(Settled)
            }
        }
        StartToEnd, Settled -> {}
    }


    SwipeToDismissBox(
        state = dismissState,
        backgroundContent = { DismissBackground(dismissState) },
        enableDismissFromStartToEnd = false,
        enableDismissFromEndToStart = true,
        content = {
            ListItem(
                modifier = Modifier.clip(RoundedCornerShape(4.dp)),
                colors = ListItemDefaults.colors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
                headlineContent = {
                    Text(
                        text = aircraft.name,
                        style = MaterialTheme.typography.bodyLarge
                    )
                },
                supportingContent = {
                    Text(
                        text = aircraft.registration,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.outline
                    )
                },
                trailingContent = {
                    val icon = if (aircraft.favorite) {
                        R.drawable.star_filled
                    } else {
                        R.drawable.star_outlined
                    }

                    IconButton(onClick = { onStarClicked(aircraft) }) {
                        Icon(
                            modifier = Modifier.size(24.dp),
                            imageVector = ImageVector.vectorResource(icon),
                            contentDescription = stringResource(R.string.profile_set_default),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            )
        }
    )
}