package com.seiberl.protrackreader.ui

import androidx.navigation.NavController

class NavigationActions(private val navController: NavController) {
    fun navigateTo(destination: NavigationBarItem) {
        navController.navigate(destination.screen) {
            popUpTo(navController.graph.startDestinationId) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }
}