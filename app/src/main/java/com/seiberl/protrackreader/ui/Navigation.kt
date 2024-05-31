package com.seiberl.protrackreader.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.seiberl.protrackreader.ui.Screen.JumpDetailScreen
import com.seiberl.protrackreader.ui.Screen.JumpListScreen

@Composable
fun Navigation() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = JumpListScreen
    ) {
        composable<JumpListScreen> {
            //JumpListScreen()
        }

        composable<JumpDetailScreen> {
            val args = it.toRoute<JumpDetailScreen>()
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "jump nr: ${args.jumpNr}")
                Button(onClick = { /*TODO*/ }) {
                    Text(text = "Go to screen list")
                }
            }
        }
    }
}