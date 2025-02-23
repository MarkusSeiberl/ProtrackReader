package com.seiberl.protrackreader.ui.profile.dropzone

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
import com.seiberl.protrackreader.R
import com.seiberl.protrackreader.persistance.entities.Dropzone
import com.seiberl.protrackreader.ui.profile.DismissBackground

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropzoneScreen(
    navController: NavController,
    viewModel: DropzoneViewModel = hiltViewModel()
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
                    Text(text = stringResource(id = R.string.profile_dropzone_label))
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
                onClick = viewModel::onAddDropzoneClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .systemBarsPadding()
                    .padding(16.dp)
            ) {
                Text(stringResource(R.string.profile_dropzone_add))
            }
        },
    ) { padding ->
        DropzoneScrollContent(padding, viewModel)
    }
}

@Composable
fun DropzoneScrollContent(padding: PaddingValues, viewModel: DropzoneViewModel) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    if (uiState.showAddDropzoneDialog) {
        AddDropzoneDialog(
            onConfirm = { dropzone -> viewModel.addDropzone(dropzone) },
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
            itemsIndexed(uiState.dropzone) { index, dropzone ->
                DropzoneItem(
                    dropzone,
                    onStarClicked = { favDropzone -> viewModel.starDropzone(favDropzone) },
                    onRemoveClicked = { favDropzone -> viewModel.removeDropzone(favDropzone) }
                )
                if (index < uiState.dropzone.lastIndex) {
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
fun DropzoneItem(
    dropzone: Dropzone,
    onStarClicked: (Dropzone) -> Unit,
    onRemoveClicked: (Dropzone) -> Unit
) {

    val dismissState = rememberSwipeToDismissBoxState()

    when (dismissState.currentValue) {
        EndToStart -> {
            LaunchedEffect(dismissState) {
                onRemoveClicked(dropzone)
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
                        text = dropzone.name,
                        style = MaterialTheme.typography.bodyLarge
                    )
                },
                supportingContent = {
                    Text(
                        text = dropzone.location,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.outline
                    )
                },
                trailingContent = {
                    val icon = if (dropzone.favorite) {
                        R.drawable.star_filled
                    } else {
                        R.drawable.star_outlined
                    }

                    IconButton(onClick = { onStarClicked(dropzone) }) {
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