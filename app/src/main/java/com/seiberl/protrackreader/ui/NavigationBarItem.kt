package com.seiberl.protrackreader.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.ui.graphics.vector.ImageVector
import com.seiberl.protrackreader.R


sealed class NavigationBarItem(
    val index: Int,
    val screen: Screen,
    val icon: ImageVector,
    val title: Int
) {
    data object Home : NavigationBarItem(
        index = 0,
        screen = Screen.JumpListScreen,
        icon = Icons.AutoMirrored.Default.List,
        title = R.string.navigation_home_label
    )

    data object Dropzone : NavigationBarItem(
        index = 1,
        screen = Screen.DropzoneScreen,
        icon = Icons.Default.LocationOn,
        title = R.string.navigation_dropzone_label
    )

    data object Aircraft : NavigationBarItem(
        index = 2,
        screen = Screen.AircraftScreen,
        icon = Icons.Default.ShoppingCart,
        title = R.string.navigation_aircraft_label
    )

    data object Profile : NavigationBarItem(
        index = 3,
        screen = Screen.ProfileScreen,
        icon = Icons.Default.AccountCircle,
        title = R.string.navigation_profile_label
    )
}


val NAVIGATION_BAR_ITEMS = listOf(
    NavigationBarItem.Home,
    NavigationBarItem.Dropzone,
    NavigationBarItem.Aircraft,
    NavigationBarItem.Profile
)