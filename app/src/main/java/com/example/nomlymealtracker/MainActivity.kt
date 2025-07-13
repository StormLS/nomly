package com.example.nomlymealtracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.compose.rememberNavController
import com.example.nomlymealtracker.ui.navigation.NavGraph
import com.example.nomlymealtracker.ui.theme.NomlyMealTrackerTheme

class MainActivity : ComponentActivity()
{

    // Main Activity of the application and its entry point
    // Where I've declared my snackBarHost, scope and navController for the application
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NomlyMealTrackerTheme {
                val snackbarHostState = remember { SnackbarHostState() }
                val scope = rememberCoroutineScope()
                val navController = rememberNavController()
                NavGraph(navController = navController, snackbarHostState = snackbarHostState, scope = scope)
            }
        }
    }
}