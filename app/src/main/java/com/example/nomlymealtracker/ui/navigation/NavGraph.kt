package com.example.nomlymealtracker.ui.navigation

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.nomlymealtracker.ui.screens.auth.ForgotPasswordScreen
import com.example.nomlymealtracker.ui.screens.auth.LoginScreen
import com.example.nomlymealtracker.ui.screens.auth.RegisterScreen
import com.example.nomlymealtracker.ui.screens.home.HomeScreen
import com.example.nomlymealtracker.ui.screens.home.HomeScreenContent
import kotlinx.coroutines.CoroutineScope

@Composable
fun NavGraph(
    navController: NavHostController,
    snackbarHostState: SnackbarHostState,
    scope: CoroutineScope)
{
    NavHost(navController = navController, startDestination = Screen.Login.route){

        composable(Screen.Login.route) {
            LoginScreen(
                snackbarHost = snackbarHostState,
                scope = scope,
                onLoginSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onRegisterClick = {
                    navController.navigate(Screen.Register.route)
                },
                onForgotPasswordClick = {
                    navController.navigate(Screen.ForgotPassword.route)
                }
            )
        }

        composable(Screen.Register.route) {
            RegisterScreen(
                navController = navController,
                snackbarHost = snackbarHostState,
                scope = scope
            )
        }

        composable(Screen.ForgotPassword.route) {
            ForgotPasswordScreen(
                navController = navController,
                snackbarHost = snackbarHostState,
                scope = scope
            )
        }

        composable(Screen.Home.route) {
            HomeScreen(
                navController = navController,
                snackbarHost = snackbarHostState,
                coroutineScope = scope,
//                onLogout = {
//                    navController.navigate(Screen.Login.route) {
//                        popUpTo(0) // clears back stack
//                    }
//                }
            )

        }
    }
}