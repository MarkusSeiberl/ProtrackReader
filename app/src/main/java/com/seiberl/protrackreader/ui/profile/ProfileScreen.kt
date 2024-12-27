package com.seiberl.protrackreader.ui.profile

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.seiberl.protrackreader.R

@Composable
fun ProfileScreen() {
    // Initial generierter Name
    var username by remember { mutableStateOf(generateRandomName()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp, 64.dp, 16.dp, 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Box(
            modifier = Modifier
                .size(100.dp)
                .background(Color.Gray, shape = CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = username.take(2).uppercase(),
                fontSize = 36.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(24.dp))


        OutlinedTextField(
            value = username,
            label = { Text(stringResource(R.string.profile_username_label)) },
            leadingIcon = { Icon(Icons.Filled.Person, null) },
            maxLines = 1,
            onValueChange = { newValue -> username = newValue },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            textStyle = LocalTextStyle.current
        )

        Spacer(modifier = Modifier.height(24.dp))

        val canopyIcon = ImageVector.vectorResource(R.drawable.canopy)
        val aircraftIcon = ImageVector.vectorResource(R.drawable.aircraft)

        SettingsItemSection(
            ProfileItem(
                canopyIcon,
                R.string.profile_canopy_label,
                "Set default canopy. Add new canopy.",
                null
            ),
            ProfileItem(
                aircraftIcon,
                R.string.profile_aircraft_label,
                "Set default aircraft. Add new aircraft.",
                null
            ),
            ProfileItem(
                Icons.Default.LocationOn,
                R.string.profile_dropzone_label,
                "Set default dropzone. Add new dropzones.",
                {})
        )
    }
}

@Composable
fun SettingsItemSection(vararg profileItems: ProfileItem) {

    Column(modifier = Modifier
        .background(MaterialTheme.colorScheme.surfaceContainer, RoundedCornerShape(8.dp))
    ) {
        profileItems.forEachIndexed { index, item ->
            Column {
                SettingsItem(item.icon, item.title, item.subtitle, item.showMore)
                if (index < profileItems.size - 1) {
                    HorizontalDivider(
                        modifier = Modifier
                            .padding(56.dp, 0.dp, 16.dp, 0.dp),
                        thickness = 1.dp,
                        color = MaterialTheme.colorScheme.outline
                    )
                }
            }
        }
    }
}

@Composable
fun SettingsItem(icon: ImageVector, @StringRes title: Int, subtitle: String, showMore: (() -> Unit)?) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { showMore?.invoke() },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = stringResource(title),
            tint = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier
                .background(MaterialTheme.colorScheme.primary, shape = RoundedCornerShape(8.dp))
                .padding(8.dp)
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column {
            Text(text = stringResource(title), fontSize = 16.sp, color = Color.Black)
            Text(text = subtitle, fontSize = 12.sp, color = Color.Gray)
        }

        if (showMore != null) {
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                imageVector = Icons.AutoMirrored.Default.KeyboardArrowRight,
                contentDescription = "Show more"
            )
        }
    }
}

fun generateRandomName(): String {
    val adjectives = listOf("Happy", "Bright", "Lucky", "Brave", "Swift")
    val nouns = listOf("Fox", "Eagle", "Tiger", "Wolf", "Bear")
    return "${adjectives.random()} ${nouns.random()}"
}
