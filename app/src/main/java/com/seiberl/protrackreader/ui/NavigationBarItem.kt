package com.seiberl.protrackreader.ui

import androidx.annotation.DrawableRes
import com.seiberl.protrackreader.R


sealed class NavigationBarItem(
    val screen: Screen,
    @get:DrawableRes val icon: Int,
    val title: Int,
    var route: String
) {
    data object Home : NavigationBarItem(
        screen = Screen.JumpListScreen,
        icon = R.drawable.list,
        title = R.string.navigation_home_label,
        route = Screen.JumpListScreen::class.java.name.toRoute()
    )

    data object Profile : NavigationBarItem(
        screen = Screen.ProfileScreen,
        icon = R.drawable.account_circle,
        title = R.string.navigation_profile_label,
        route = Screen.ProfileScreen::class.java.name.toRoute()
    )
}


val NAVIGATION_BAR_ITEMS = listOf(
    NavigationBarItem.Home,
    NavigationBarItem.Profile
)

private fun String.toRoute() = replace("$", ".")