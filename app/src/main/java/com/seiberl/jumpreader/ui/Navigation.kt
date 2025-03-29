package com.seiberl.jumpreader.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.seiberl.jumpreader.ui.home.JumpListScreen
import com.seiberl.jumpreader.ui.home.JumpListViewModel
import com.seiberl.jumpreader.ui.jumpdetail.JumpDetailScreen
import com.seiberl.jumpreader.ui.profile.ProfileScreen
import com.seiberl.jumpreader.ui.profile.aircraft.AircraftScreen
import com.seiberl.jumpreader.ui.profile.canopy.CanopyScreen
import com.seiberl.jumpreader.ui.profile.dropzone.DropzoneScreen

@Composable
fun Navigation(jumpListViewModel: JumpListViewModel) {
    val navController = rememberNavController()
    val navigationActions = remember(navController) {
        NavigationActions(navController)
    }
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val selectedDestination =
        navBackStackEntry?.destination?.route ?: NAVIGATION_BAR_ITEMS.first().route

    Column(
        verticalArrangement = Arrangement.Bottom,
        modifier = Modifier.fillMaxSize()
    ) {
        NavHost(
            modifier = Modifier.weight(1f),
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

        CustomNavigationBar(navigationActions::navigateTo, selectedDestination)
    }
}