package com.seiberl.protrackreader.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.seiberl.protrackreader.ui.aircrafts.AircraftScreen
import com.seiberl.protrackreader.ui.dropzone.DropzoneScreen
import com.seiberl.protrackreader.ui.home.JumpListScreen
import com.seiberl.protrackreader.ui.jumpdetail.JumpDetailScreen
import com.seiberl.protrackreader.ui.profile.ProfileScreen

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Navigation() {
    val navController = rememberNavController()
    val navigationActions = remember(navController) {
        NavigationActions(navController)
    }
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val selectedDestination =
        navBackStackEntry?.destination?.route ?: NAVIGATION_BAR_ITEMS.first().route

    Scaffold(
        bottomBar = { CustomNavigationBar(navigationActions::navigateTo, selectedDestination) }
    ) { padding ->
        NavHost(
            modifier = Modifier.padding(padding),
            navController = navController,
            startDestination = Screen.JumpListScreen
        ) {
            composable<Screen.JumpListScreen> { JumpListScreen(navController = navController) }

            composable<Screen.DropzoneScreen> { DropzoneScreen(navController = navController) }

            composable<Screen.AircraftScreen> { AircraftScreen() }

            composable<Screen.ProfileScreen> { ProfileScreen() }

            composable<Screen.JumpDetailScreen> {
                val args = it.toRoute<Screen.JumpDetailScreen>()
                JumpDetailScreen(args.jumpNr, navController)
            }
        }
    }
}