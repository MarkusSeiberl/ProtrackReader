package com.seiberl.protrackreader.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.ui.graphics.vector.ImageVector
import com.seiberl.protrackreader.R


sealed class NavigationBarItem(
    val screen: Screen,
    val icon: ImageVector,
    val title: Int,
    var route: String
) {
    data object Home : NavigationBarItem(
        screen = Screen.JumpListScreen,
        icon = Icons.AutoMirrored.Default.List,
        title = R.string.navigation_home_label,
        route = Screen.JumpListScreen::class.java.name.toRoute()
    )

    data object Dropzone : NavigationBarItem(
        screen = Screen.DropzoneScreen,
        icon = Icons.Default.LocationOn,
        title = R.string.navigation_dropzone_label,
        route = Screen.DropzoneScreen::class.java.name.toRoute()
    )

    data object Aircraft : NavigationBarItem(
        screen = Screen.AircraftScreen,
        icon = Icons.Default.ShoppingCart,
        title = R.string.navigation_aircraft_label,
        route = Screen.AircraftScreen::class.java.name.toRoute()
    )

    data object Profile : NavigationBarItem(
        screen = Screen.ProfileScreen,
        icon = Icons.Default.AccountCircle,
        title = R.string.navigation_profile_label,
        route = Screen.ProfileScreen::class.java.name.toRoute()
    )
}


val NAVIGATION_BAR_ITEMS = listOf(
    NavigationBarItem.Home,
    NavigationBarItem.Dropzone,
    NavigationBarItem.Aircraft,
    NavigationBarItem.Profile
)

private fun String.toRoute() = replace("$", ".")