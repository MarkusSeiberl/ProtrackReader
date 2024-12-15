package com.seiberl.protrackreader.ui

import kotlinx.serialization.Serializable

sealed class Screen {
    @Serializable
    data object JumpListScreen : Screen()

    @Serializable
    data object ProfileScreen : Screen()

    @Serializable
    data class JumpDetailScreen(
        val jumpNr: Int
    ) : Screen()

}