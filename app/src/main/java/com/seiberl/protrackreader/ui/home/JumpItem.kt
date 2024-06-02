package com.seiberl.protrackreader.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.seiberl.protrackreader.persistance.entities.Jump

@Composable
fun JumpItem(jump: Jump) {

    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .height(IntrinsicSize.Min),
        verticalAlignment = Alignment.CenterVertically
    ) {
        JumpNumber(number = jump.number)

        Column(
            modifier = Modifier,
            horizontalAlignment = Alignment.Start
        ) {
            Text("${jump.exitAltitude}")
            Text("Exit")
        }

        Text(text = "–", style = MaterialTheme.typography.headlineMedium)

        Column(
            modifier = Modifier.padding(4.dp),
            horizontalAlignment = Alignment.End
        ) {
            Text("${jump.deploymentAltitude}")
            Text("DPL")
        }

        VerticalDivider(modifier = Modifier.fillMaxHeight(0.8f).padding(4.dp), color = MaterialTheme.colorScheme.secondary)
        Text(text = "sdf")
    }

}


@Composable
fun JumpNumber(number: Int) {
    Box(modifier = Modifier
        .padding(8.dp)
        .clip(RoundedCornerShape(10.dp))
        .background(Color.LightGray)
    ) {
        Text(
            modifier = Modifier.padding(8.dp),
            text = "$number",
            style = MaterialTheme.typography.headlineLarge
        )
    }
}