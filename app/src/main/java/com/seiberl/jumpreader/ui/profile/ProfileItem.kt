package com.seiberl.jumpreader.ui.profile

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.vector.ImageVector

data class ProfileItem(val icon: ImageVector, @StringRes val title: Int, val subtitle: String, val showMore: (() -> Unit)?)