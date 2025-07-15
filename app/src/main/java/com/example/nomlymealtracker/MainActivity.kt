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

/**
 * MainActivity serves as the entry point of the Nomly Meal Tracker app.
 * It sets up the app's theme, navigation, snackbar host, and coroutine scope for global UI logic.
 */
class MainActivity : ComponentActivity()
{
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