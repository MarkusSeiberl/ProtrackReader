package com.seiberl.protrackreader.ui

import android.graphics.drawable.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import com.seiberl.protrackreader.R

sealed interface NavigationBarItem {
    val index: Int
    val screen: Screen
    val icon: ImageVector
    val title: Int
}

class NavigationBarItems {

    val items = listOf(Home, Dropzone, Aircraft, Profile)

    data object Home: NavigationBarItem {
        override val index: Int = 0
        override val screen: Screen = Screen.JumpListScreen
        override val icon: ImageVector = Icons.AutoMirrored.Default.List
        override val title: Int = R.string.navigation_home_label
    }
    data object Dropzone: NavigationBarItem {
        override val index: Int = 1
        override val screen: Screen = Screen.DropzoneScreen
        override val icon: ImageVector = Icons.Default.LocationOn
        override val title: Int = R.string.navigation_dropzone_label
    }
    data object Aircraft: NavigationBarItem {
        override val index: Int = 2
        override val screen: Screen = Screen.AircraftScreen
        override val icon: ImageVector = Icons.Default.ShoppingCart
        override val title: Int = R.string.navigation_aircraft_label
    }
    data object Profile: NavigationBarItem {
        override val index: Int = 3
        override val screen: Screen = Screen.ProfileScreen
        override val icon: ImageVector = Icons.Default.AccountCircle
        override val title: Int = R.string.navigation_profile_label
    }
}