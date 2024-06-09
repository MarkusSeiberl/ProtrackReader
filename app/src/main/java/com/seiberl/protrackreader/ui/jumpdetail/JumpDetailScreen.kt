package com.seiberl.protrackreader.ui.jumpdetail

import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.seiberl.protrackreader.ui.Screen

@Composable
fun JumpDetailScreen(
    jumpNr: Int,
    navController: NavController,
    viewModel: JumpDetailViewModel = hiltViewModel()
) {
    viewModel.loadJump(jumpNr)

    val verticalScrollState = rememberScrollState()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(modifier = Modifier.verticalScroll(verticalScrollState)) {
        Text(text = "${uiState.jump?.number ?: "--"}")
    }
}