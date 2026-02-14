package com.example.nomlymealtracker.ui.navigation

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.nomlymealtracker.helper.SessionManager
import com.example.nomlymealtracker.ui.screens.addMeal.AddMealScreen
import com.example.nomlymealtracker.ui.screens.auth.ForgotPasswordScreen
import com.example.nomlymealtracker.ui.screens.auth.LoginScreen
import com.example.nomlymealtracker.ui.screens.auth.RegisterScreen
import com.example.nomlymealtracker.ui.screens.home.HomeScreen
import com.example.nomlymealtracker.ui.screens.viewMeal.ViewMealScreen
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope

/**
 * Composable function that defines the navigation graph for the application using [NavHost].
 * This includes routes for login, registration, password reset, home, add meal, and view meal.
 * @param navController The [NavHostController] used to navigate between composable screens.
 * @param snackbarHostState A shared [SnackbarHostState] used to show snackbars from various screens.
 * @param scope The [CoroutineScope] used to launch suspend functions such as snackbar messages.
 */
@Composable
fun NavGraph(
    navController: NavHostController,
    snackbarHostState: SnackbarHostState,
    scope: CoroutineScope)
{
    val context = LocalContext.current
    val currentUser = FirebaseAuth.getInstance().currentUser
    val rememberMe = SessionManager.isRememberMeEnabled(context)

    // If the user selected "Remember Me" and Firebase still has a valid session,
    // skip the login screen and go directly to Home.
    val startDestination = if (rememberMe && currentUser != null) {
        Screen.Home.route
    } else {
        // Clear any stale Firebase session if Remember Me was not selected
        if (currentUser != null) {
            FirebaseAuth.getInstance().signOut()
        }
        Screen.Login.route
    }

    NavHost(navController = navController, startDestination = startDestination){
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
                coroutineScope = scope
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
                onAddNewMealClick = {
                    navController.navigate(Screen.AddMeal.route)
                },
                onLogoutClick = {
                    navController.navigate("login") {
                        popUpTo("home") { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.AddMeal.route){
            AddMealScreen(
                snackbarHost = snackbarHostState,
                coroutineScope = scope,
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            Screen.ViewMeal.route + "/{mealId}",
            arguments = listOf(navArgument("mealId") { type = NavType.StringType })
        ){ backStackEntry ->
            val mealId = backStackEntry.arguments?.getString("mealId") ?: return@composable
            ViewMealScreen(
                mealId = mealId,
                snackbarHost = snackbarHostState,
                coroutineScope = scope,
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}