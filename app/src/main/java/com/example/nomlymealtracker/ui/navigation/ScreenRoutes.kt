package com.example.nomlymealtracker.ui.navigation

/**
 * A sealed class representing all navigation destinations in the app.
 * Each object holds a unique [route] string used by the Navigation component.
 */
sealed class Screen(val route: String) {
    data object Login : Screen("login")
    data object Register : Screen("register")
    data object ForgotPassword : Screen("forgot_password")
    data object Home : Screen("home")
    data object AddMeal : Screen("add_meal")
    data object ViewMeal : Screen("view_meal")
}