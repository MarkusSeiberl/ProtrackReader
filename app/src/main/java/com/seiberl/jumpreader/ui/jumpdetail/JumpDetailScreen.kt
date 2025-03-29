package com.seiberl.jumpreader.ui.jumpdetail

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.window.core.layout.WindowHeightSizeClass
import androidx.window.core.layout.WindowSizeClass
import androidx.window.core.layout.WindowWidthSizeClass
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberBottom
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberStart
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLineCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.compose.cartesian.rememberVicoScrollState
import com.patrykandpatrick.vico.compose.cartesian.rememberVicoZoomState
import com.patrykandpatrick.vico.core.cartesian.Zoom
import com.patrykandpatrick.vico.core.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.core.cartesian.axis.VerticalAxis
import com.seiberl.jumpreader.R
import com.seiberl.jumpreader.persistance.entities.Aircraft
import com.seiberl.jumpreader.persistance.entities.Canopy
import com.seiberl.jumpreader.persistance.entities.Dropzone
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JumpDetailScreen(
    jumpNr: Int,
    navController: NavController,
    viewModel: JumpDetailViewModel = hiltViewModel(),
    windowSizeClass: WindowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
) {
    viewModel.loadJump(jumpNr)

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val verticalScrollState = rememberScrollState()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val showFullScreen =
        windowSizeClass.windowWidthSizeClass == WindowWidthSizeClass.EXPANDED ||
                windowSizeClass.windowHeightSizeClass == WindowHeightSizeClass.COMPACT

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    if (showFullScreen) {
                        Text(text = stringResource(id = R.string.jumpdetail_title, jumpNr))
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Localized description"
                        )
                    }
                },
                scrollBehavior = scrollBehavior,
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .background(MaterialTheme.colorScheme.surface)
                .fillMaxSize()
                .verticalScroll(verticalScrollState)
                .height(IntrinsicSize.Max),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            if (showFullScreen) {
                // If the phone is in landscape display only the chart
                JumpChart(Modifier.fillMaxSize(), viewModel)

            } else {
                val jump = uiState.jumpDetail?.jump
                JumpTitle(jumpNr)

                JumpDateTime(jump?.timestamp ?: Instant.now())

                val exit = jump?.exitAltitude ?: 0
                val deployment = jump?.deploymentAltitude ?: 0
                val freefall = jump?.freefallTime ?: 0
                val maxSpeed = ((jump?.maxSpeed ?: 0) * 3.6).toInt()
                val avgSpeed = ((jump?.averageSpeed ?: 0) * 3.6).toInt()
                JumpStatistics(exit, deployment, freefall, maxSpeed, avgSpeed)
                JumpFavorites(uiState.jumpDetail?.aircraft, uiState.jumpDetail?.canopy, uiState.jumpDetail?.dropzone)

                JumpChart(Modifier.fillMaxHeight(0.75f), viewModel)
            }
        }
    }
}

@Composable
fun JumpTitle(jumpNr: Int = 0) {
    Text(
        text = "Jump Nr.",
        style = MaterialTheme.typography.headlineLarge,
        color = MaterialTheme.colorScheme.primary,
        fontSize = MaterialTheme.typography.headlineLarge.fontSize * 2
    )
    Spacer(modifier = Modifier.size(8.dp))
    Text(
        text = "$jumpNr",
        style = MaterialTheme.typography.headlineLarge,
        color = MaterialTheme.colorScheme.primary,
        fontSize = MaterialTheme.typography.headlineLarge.fontSize * 1.4
    )
}

@Composable
fun JumpDateTime(timestamp: Instant) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        val timeFormatter = DateTimeFormatter.ofPattern("HH:mm").withZone(ZoneId.systemDefault())
        val dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yy").withZone(ZoneId.systemDefault())
        val time = timeFormatter.format(timestamp)
        val date = dateFormatter.format(timestamp)
        Text(text = date )
        Text(text = time )
    }
}

@Composable
fun JumpStatistics(
    exitAltitude: Int = 0,
    deploymentAltitude: Int = 0,
    freefallTime: Int = 0,
    maxSpeed: Int = 0,
    avgSpeed: Int = 0
) {
    Row(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.medium)
            .background(MaterialTheme.colorScheme.tertiaryContainer)
            .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Row {
                Icon(modifier = Modifier.padding(end = 8.dp),
                    painter =painterResource(R.drawable.analytics),
                    contentDescription = null
                )
                Text(
                    text = stringResource(R.string.jumpdetail_info_title_statistics),
                    style = MaterialTheme.typography.titleMedium
                )
            }

            Spacer(modifier = Modifier.size(8.dp))

            Text(stringResource(R.string.jumpdetail_info_title_exit))
            Text(stringResource(R.string.jumpdetail_info_title_deployment))
            Text(stringResource(R.string.jumpdetail_info_title_freefall))
            Text(stringResource(R.string.jumpdetail_info_title_maxspeed))
            Text(stringResource(R.string.jumpdetail_info_title_avgspeed))
        }

        Column(modifier = Modifier, horizontalAlignment = Alignment.End) {
            Text("")
            Spacer(modifier = Modifier.size(8.dp))

            Text(stringResource(R.string.jumpdetail_info_content_meter, exitAltitude))
            Text(stringResource(R.string.jumpdetail_info_content_meter, deploymentAltitude))
            Text(stringResource(R.string.jumpdetail_info_content_seconds, freefallTime))
            Text(stringResource(R.string.jumpdetail_info_content_speed_kmh, maxSpeed))
            Text(stringResource(R.string.jumpdetail_info_content_speed_kmh, avgSpeed))
        }
    }
}

@Composable
fun JumpFavorites(aircraft: Aircraft?, canopy: Canopy?, dropzone: Dropzone?) {
    if (aircraft == null && canopy == null && dropzone == null) return

    Row(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.medium)
            .background(MaterialTheme.colorScheme.tertiaryContainer)
            .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Row {
                Icon(modifier = Modifier.padding(end = 8.dp),
                    painter =painterResource(R.drawable.analytics),
                    contentDescription = null
                )
                Text(
                    text = stringResource(R.string.jumpdetail_meta_title),
                    style = MaterialTheme.typography.titleMedium
                )
            }

            Spacer(modifier = Modifier.size(8.dp))

            Text(stringResource(R.string.jumpdetail_meta_aircraft))
            Text(stringResource(R.string.jumpdetail_meta_canopy))
            Text(stringResource(R.string.jumpdetail_meta_dropzone))
        }

        Column(modifier = Modifier, horizontalAlignment = Alignment.End) {
            Text("")
            Spacer(modifier = Modifier.size(8.dp))

            Text(aircraft?.name ?: "")
            Text(canopy?.name ?: "")
            Text(dropzone?.name ?: "")
        }
    }
}

@Composable
fun JumpChart(modifier: Modifier = Modifier,viewModel: JumpDetailViewModel) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val scrollState = rememberVicoScrollState()
    val zoomState = rememberVicoZoomState(initialZoom = Zoom.Content)
    Log.d("TAAG", "zoom ${zoomState.value}")
    Log.d("TAAG", "zoomr ${zoomState.valueRange}")
    Log.d("TAAG", "samples ${uiState.jumpDetail?.jump?.sampleSize}")


    var shownSampleCount = 10.0 / zoomState.value
    if (shownSampleCount == 0.0 || shownSampleCount.isNaN() || shownSampleCount.isInfinite()) shownSampleCount =
        10.0
    var spacing = shownSampleCount / 5
    if (spacing < 1.0 || spacing.isNaN() || spacing.isInfinite()) spacing = 1.0
    Log.d("TAAG", "shown $shownSampleCount and spacing ${spacing.toInt()}")

    Row (modifier = modifier) {
        CartesianChartHost(
            modifier = Modifier.padding(16.dp, 0.dp, 16.dp, 16.dp).fillMaxSize(),
            chart = rememberCartesianChart(
                rememberLineCartesianLayer(),
                startAxis = VerticalAxis.rememberStart(guideline = null),
                bottomAxis = HorizontalAxis.rememberBottom(
                    guideline = null,
                    itemPlacer = HorizontalAxis.ItemPlacer.aligned(
                        spacing = { spacing.toInt() },
                        addExtremeLabelPadding = false
                    ),
                )
            ),
            zoomState = zoomState,
            scrollState = scrollState,
            modelProducer = uiState.chartModel
        )
    }
}