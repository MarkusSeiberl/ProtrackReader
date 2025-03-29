package com.seiberl.jumpreader.ui

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

@Keep
sealed class Screen {
    @Keep
    @Serializable
    data object JumpListScreen : Screen()

    @Keep
    @Serializable
    data object ProfileScreen : Screen()

    @Keep
    @Serializable
    data object AircraftScreen : Screen()

    @Keep
    @Serializable
    data object CanopyScreen : Screen()

    @Keep
    @Serializable
    data object DropzoneScreen : Screen()

    @Keep
    @Serializable
    data class JumpDetailScreen(
        val jumpNr: Int
    ) : Screen()

}