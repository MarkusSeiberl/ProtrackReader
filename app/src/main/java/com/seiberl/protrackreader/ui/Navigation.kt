package com.seiberl.protrackreader.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.seiberl.protrackreader.ui.home.JumpListScreen
import com.seiberl.protrackreader.ui.home.JumpListViewModel
import com.seiberl.protrackreader.ui.jumpdetail.JumpDetailScreen
import com.seiberl.protrackreader.ui.profile.ProfileScreen
import com.seiberl.protrackreader.ui.profile.aircraft.AircraftScreen
import com.seiberl.protrackreader.ui.profile.canopy.CanopyScreen
import com.seiberl.protrackreader.ui.profile.dropzone.DropzoneScreen

@Composable
fun Navigation(jumpListViewModel: JumpListViewModel) {
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
            composable<Screen.JumpListScreen> {
                JumpListScreen(jumpListViewModel, navController = navController)
            }

            composable<Screen.JumpDetailScreen> {
                val args = it.toRoute<Screen.JumpDetailScreen>()
                JumpDetailScreen(args.jumpNr, navController)
            }

            composable<Screen.ProfileScreen> { ProfileScreen(navController) }

            composable<Screen.AircraftScreen> { AircraftScreen(navController) }

            composable<Screen.CanopyScreen> { CanopyScreen(navController) }

            composable<Screen.DropzoneScreen> { DropzoneScreen(navController) }
        }
    }
}