package com.seiberl.protrackreader.ui.jumpdetail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberBottomAxis
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberStartAxis
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLineCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.compose.cartesian.rememberVicoScrollState
import com.patrykandpatrick.vico.compose.cartesian.rememberVicoZoomState
import com.seiberl.protrackreader.R
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import kotlin.math.max

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JumpDetailScreen(
    jumpNr: Int,
    navController: NavController,
    viewModel: JumpDetailViewModel = hiltViewModel()
) {
    viewModel.loadJump(jumpNr)

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val verticalScrollState = rememberScrollState()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
//                    Text(
//                        stringResource(R.string.jumpdetail_title, uiState.jump?.number ?: "-"),
//                        maxLines = 1,
//                        overflow = TextOverflow.Ellipsis
//                    )
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
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "Jump Nr.",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "${uiState.jump?.number ?: "-"}",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.primary
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                val timeFormatter = DateTimeFormatter.ofPattern("HH:mm").withZone(ZoneId.systemDefault())
                val dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yy").withZone(ZoneId.systemDefault())
                val time = if (null != uiState.jump)  timeFormatter.format(uiState.jump?.timestamp) else ""
                val date = if (null != uiState.jump)  dateFormatter.format(uiState.jump?.timestamp) else ""
                Text(text = date )
                Text(text = time )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                val exit = uiState.jump?.exitAltitude ?: 0
                val deployment = uiState.jump?.deploymentAltitude ?: 0
                InfoBox(Icons.Filled.Info, R.string.jumpdetail_info_title_exit, exit, R.string.jumpdetail_info_content_meter)
                InfoBox(Icons.Filled.Info, R.string.jumpdetail_info_title_deployment, deployment, R.string.jumpdetail_info_content_meter)
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                val freefall = uiState.jump?.freefallTime ?: 0
                InfoBox(Icons.Filled.Info, R.string.jumpdetail_info_title_freefall, freefall, R.string.jumpdetail_info_content_seconds)
            }


            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                val maxSpeed = ((uiState.jump?.maxSpeed ?: 0) * 3.6).toInt()
                val avgSpeed = ((uiState.jump?.averageSpeed ?: 0) * 3.6).toInt()
                InfoBox(Icons.Filled.Info, R.string.jumpdetail_info_title_maxspeed, maxSpeed, R.string.jumpdetail_info_content_speed_kmh)
                InfoBox(Icons.Filled.Info, R.string.jumpdetail_info_title_avgspeed, avgSpeed, R.string.jumpdetail_info_content_speed_kmh)
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                val scrollState = rememberVicoScrollState()
                val zoomState = rememberVicoZoomState()

                CartesianChartHost(
                    chart = rememberCartesianChart(
                        rememberLineCartesianLayer(),
                        startAxis = rememberStartAxis(),
                        bottomAxis = rememberBottomAxis()
                    ),
                    zoomState = zoomState,
                    scrollState = scrollState,
                    modelProducer = uiState.chartModel
                )

            }


        }
    }
}