package com.example.nomlymealtracker.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.nomlymealtracker.ui.screens.auth.LoginScreen
import com.example.nomlymealtracker.ui.screens.home.HomeScreen

@Composable
fun NavGraph(navController: NavHostController)
{
    NavHost(navController = navController, startDestination = Screen.Login.route){
        composable(Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                //TODO: Add register and forgot password
//                onNavigateToRegister = {
//                    navController.navigate(Screen.Register.route)
//                },
//                onNavigateToForgotPassword = {
//                    navController.navigate(Screen.ForgotPassword.route)
//                }
            )
        }

        composable(Screen.Home.route) {
            HomeScreen(
                // TODO: Implement some form of Logout
//                onLogout = {
//                    navController.navigate(Screen.Login.route) {
//                        popUpTo(0) // clears back stack
//                    }
//                }
            )
        }
    }
}