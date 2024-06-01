package com.seiberl.protrackreader.ui.jumpimport.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.seiberl.protrackreader.ui.jumpimport.JumpImportViewModel

@Composable
fun ImportScreen(viewModel: JumpImportViewModel, windowsWidth: WindowWidthSizeClass) {
    when (windowsWidth) {
        WindowWidthSizeClass.Expanded -> Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.Absolute.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            ImageSection(
                Modifier
                    .weight(0.4f)
                    .padding(32.dp)
            )
            DescriptionSection(
                Modifier
                    .weight(0.6f)
                    .padding(start = 32.dp, top = 32.dp, end = 32.dp)
                    .verticalScroll(rememberScrollState()),
                viewModel,
                windowsWidth
            )
        }

        WindowWidthSizeClass.Compact, WindowWidthSizeClass.Medium -> Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            ImageSection(
                Modifier
                    .weight(0.4f)
                    .padding(top = 64.dp)
            )
            DescriptionSection(
                Modifier
                    .weight(0.6f)
                    .padding(horizontal = 32.dp)
                    .verticalScroll(rememberScrollState()),
                viewModel,
                windowsWidth
            )
        }
    }
}