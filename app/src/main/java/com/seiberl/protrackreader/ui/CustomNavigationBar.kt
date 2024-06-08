package com.seiberl.protrackreader.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController

@Composable
fun CustomNavigationBar(navController: NavController) {

    var selectedItem by remember { mutableIntStateOf(0) }

    fun onNavigationItemClick(item: NavigationBarItem) {
        if (selectedItem == item.index) return // Ignore if already selected

        selectedItem = item.index
        navController.navigate(item.screen) {
            popUpTo(navController.graph.startDestinationId) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }

    NavigationBar(modifier = Modifier.fillMaxWidth()) {
        NAVIGATION_BAR_ITEMS.forEach { item ->
            NavigationBarItem(
                selected = selectedItem == item.index,
                onClick = { onNavigationItemClick(item) },
                icon = { Icon(item.icon, "") },
                label = { Text(stringResource(id = item.title)) }
            )
        }
    }
}
