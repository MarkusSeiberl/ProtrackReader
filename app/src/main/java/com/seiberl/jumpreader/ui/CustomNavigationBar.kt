package com.seiberl.jumpreader.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource

@Composable
fun CustomNavigationBar(
    navigateTo: (NavigationBarItem) -> Unit,
    selectedDestination: String
) {

    val destinations = NAVIGATION_BAR_ITEMS.map { it.route }
    if (destinations.contains(selectedDestination)) {

        NavigationBar(modifier = Modifier.fillMaxWidth()) {
            NAVIGATION_BAR_ITEMS.forEach { item ->
                NavigationBarItem(
                    selected = selectedDestination == item.route,
                    onClick = { navigateTo(item) },
                    icon = { Icon(painterResource(id = item.icon), null) },
                    label = { Text(stringResource(id = item.title)) }
                )
            }
        }
    }
}
