package com.seiberl.jumpreader.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.seiberl.jumpreader.persistance.views.JumpMetaData
import com.seiberl.jumpreader.ui.theme.primaryLight
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun JumpItem(jump: JumpMetaData, selected: Boolean, onSelected: (Int) -> Unit) {

    val backgroundColor = if (selected) {
        MaterialTheme.colorScheme.surfaceVariant
    } else {
        MaterialTheme.colorScheme.surfaceContainer
    }

    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .height(IntrinsicSize.Min)
            .padding(16.dp, 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        JumpNumber(number = jump.number, onSelected)

        Column(
            modifier = Modifier,
            horizontalAlignment = Alignment.Start
        ) {
            Text("Exit:")
            Text("DPL")
        }

        Column(
            modifier = Modifier.padding(4.dp),
            horizontalAlignment = Alignment.End
        ) {
            Text("${jump.exitAltitude.toInt()} m")
            Text("${jump.deploymentAltitude.toInt()} m")
        }

        Row (modifier = Modifier
            .fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {


            VerticalDivider(
                modifier = Modifier
                    .fillMaxHeight(0.8f)
                    .padding(16.dp, 0.dp), color = MaterialTheme.colorScheme.secondary
            )

            Column(
                horizontalAlignment = Alignment.End
            ) {
                val timeFormatter = DateTimeFormatter.ofPattern("HH:mm").withZone(ZoneId.systemDefault())
                val dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yy").withZone(ZoneId.systemDefault())
                Text(text = timeFormatter.format(jump.timestamp))
                Text(text = dateFormatter.format(jump.timestamp))

            }
        }
    }

}


@Composable
fun JumpNumber(number: Int, onSelected: (Int) -> Unit) {
    Box(modifier = Modifier
        .padding(0.dp, 0.dp, 16.dp, 0.dp)
        .border(2.dp, primaryLight, RoundedCornerShape(8.dp))
        .clip(RoundedCornerShape(8.dp))
        .clickable { onSelected(number) }
    ) {
        Text(
            modifier = Modifier.padding(8.dp),
            text = "$number",
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.primary
        )
    }
}