package com.seiberl.jumpreader.ui.jumpimport.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.seiberl.jumpreader.R

@Composable
fun ImageSection(modifier: Modifier = Modifier) {
    val image = painterResource(R.drawable.protrack2)
    Image(
        painter = image,
        contentDescription = stringResource(
            R.string.jumpimport_imagedescription_protrack
        ),
        modifier.fillMaxSize(0.8f)
    )
}