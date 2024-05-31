package com.seiberl.protrackreader.ui.jumpimport

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Arrangement.Absolute.SpaceEvenly
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass.Companion.Compact
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass.Companion.Expanded
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass.Companion.Medium
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Alignment.Companion.End
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextAlign.Companion.Center
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.seiberl.protrackreader.MainActivity
import com.seiberl.protrackreader.R
import com.seiberl.protrackreader.ui.theme.ProtrackReaderTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class JumpImportActivity : ComponentActivity() {

    private val viewModel: JumpImportViewModel by viewModels()

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("TAAG", "test")

        enableEdgeToEdge()
        setContent {
            ProtrackReaderTheme {
                val windowSize = calculateWindowSizeClass(activity = this)
                ImportScreen(viewModel, windowSize.widthSizeClass)
            }
        }
        Log.d("TAAG", "test2")
    }

    fun onShowNextActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

}

@Composable
fun ImportScreen(viewModel: JumpImportViewModel, windowsWidth: WindowWidthSizeClass) {
    when (windowsWidth) {
        Expanded -> Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = SpaceEvenly,
            verticalAlignment = CenterVertically
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
                windowsWidth
            )
        }

        Compact, Medium -> Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = CenterHorizontally,
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
                windowsWidth
            )
        }
    }
}

@Composable
private fun ImageSection(modifier: Modifier = Modifier) {
    val image = painterResource(R.drawable.protrack2)
    Image(
        painter = image,
        contentDescription = stringResource(
            R.string.jumpimport_imagedescription_protrack
        ),
        modifier.fillMaxSize(0.8f)
    )
}

@Composable
private fun DescriptionSection(
    modifier: Modifier = Modifier,
    windowWidth: WindowWidthSizeClass,
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        val titleStyle =
            if (windowWidth == Compact) {
                typography.headlineMedium
            } else {
                typography.headlineLarge
            }

        Text(
            text = stringResource(id = R.string.jumpimport_title),
            style = titleStyle,
            textAlign = Center
        )

        Spacer(modifier = Modifier.size(32.dp))

        Text(
            text = stringResource(R.string.jumpimport_body_description),
            style = typography.bodyMedium
        )

        Spacer(modifier = Modifier.size(32.dp))

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = CenterHorizontally
        ) {
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = { /*TODO*/ }
            ) {
                Text(
                    text = stringResource(R.string.jumpimport_button_import),
                    style = typography.bodyMedium,
                    textAlign = Center
                )
            }

            OutlinedButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = { /*TODO*/ }
            ) {
                Text(
                    text = stringResource(R.string.jumpimport_button_cancel),
                    style = typography.bodyMedium,
                    textAlign = Center
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ProtrackReaderTheme {
        ImportScreen(JumpImportViewModel(), Compact)
    }
}