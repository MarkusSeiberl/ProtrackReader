package com.seiberl.protrackreader.ui.jumpdetail

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource

@Composable
fun InfoBox(
    icon: ImageVector = Icons.Filled.Info,
    iconDescription: String,
    @StringRes title: Int,
    content: Int,
    @StringRes contentRes: Int
) {
    Column {
        Row {
            Icon(icon, contentDescription = "TODO")
            Text(text = stringResource(id = title))
        }
        Text(text = stringResource(id = contentRes, content))

    }
}