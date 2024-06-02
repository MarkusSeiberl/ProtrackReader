package com.seiberl.protrackreader.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.seiberl.protrackreader.ui.Screen.JumpDetailScreen
import com.seiberl.protrackreader.ui.Screen.JumpListScreen
import com.seiberl.protrackreader.ui.home.JumpListUiState
import com.seiberl.protrackreader.ui.home.JumpListViewModel
import com.seiberl.protrackreader.ui.home.JumpListScreen as JumpListContent

@Composable
fun Navigation(
    viewModel: JumpListViewModel = hiltViewModel()
) {
    val navController = rememberNavController()
    val uiState: JumpListUiState by viewModel.uiState.collectAsStateWithLifecycle()

    NavHost(
        navController = navController,
        startDestination = JumpListScreen
    ) {
        composable<JumpListScreen> {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                LazyColumn {
                    items(uiState.jumps) {jump ->
                        Text(text = "${jump.number}")
                    }
                }
            }
        }

        composable<JumpDetailScreen> {
            val args = it.toRoute<JumpDetailScreen>()
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "jump nr: ${args.jumpNr}")
                Button(onClick = { /*TODO*/ }) {
                    Text(text = "Go to screen list")
                }
            }
        }
    }
}