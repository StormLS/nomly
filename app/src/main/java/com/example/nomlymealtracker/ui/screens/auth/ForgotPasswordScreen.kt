package com.example.nomlymealtracker.ui.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.nomlymealtracker.ui.theme.LightOrange
import com.example.nomlymealtracker.ui.theme.NomlyMealTrackerTheme
import com.example.nomlymealtracker.ui.theme.Orange
import com.example.nomlymealtracker.ui.theme.PureWhite
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * Creating a Preview Friendly function for designing the Forgot Password Screen
 */
@Preview
@Composable
fun ForgotPasswordScreenPreview() {
    NomlyMealTrackerTheme {
        ForgotPasswordScreenContent(
            email = "test@example.com",
            errorMessage = "",
            successMessage = "",
            isLoading = false,
            snackbarHost = SnackbarHostState(),
            onResetEmailChange = {},
            onResetEmailClick = {},
            onBackClick = {}
        )
    }
}


/**
 * Composable that displays the Forgot Password screen, allowing the user to request a password reset link.
 * @param navController The NavController used for navigation.
 * @param snackbarHost The SnackbarHostState to show snackbars for success or error messages.
 * @param scope A CoroutineScope used to launch snackbar showing coroutines.
 * @param viewModel The AuthViewModel instance that manages the screen state and business logic.
 */
@Composable
fun ForgotPasswordScreen(
    navController: NavController,
    snackbarHost: SnackbarHostState,
    scope: CoroutineScope,
    viewModel: AuthViewModel = viewModel()
)
{
    val email = viewModel.resetEmail
    val errorMessage = viewModel.resetErrorMessage
    val successMessage = viewModel.resetSuccess
    val isLoading = viewModel.resetLoading

    LaunchedEffect(successMessage, errorMessage) {
        errorMessage?.let {
            scope.launch {
                viewModel.clearResetError()
                snackbarHost.showSnackbar(it, duration = SnackbarDuration.Short)
            }
        }

        successMessage?.let {
            scope.launch {
                viewModel.clearResetSuccess()
                snackbarHost.showSnackbar(it, duration = SnackbarDuration.Long)
            }
        }
    }

    ForgotPasswordScreenContent(
        email = email,
        errorMessage = errorMessage ?: "",
        successMessage = successMessage ?: "",
        isLoading = isLoading,
        snackbarHost = snackbarHost,
        onResetEmailChange = { viewModel.resetEmail = it },
        onResetEmailClick = { viewModel.sendPasswordResetEmail() },
        onBackClick = { navController.popBackStack() }
    )
}

/**
 * Main content composable for the Forgot Password screen.
 * Displays the UI elements including email input, buttons, and handles user interactions.
 * @param email Current email input value.
 * @param isLoading Boolean flag indicating if the reset request is in progress.
 * @param snackbarHost SnackbarHostState for showing snackbars.
 * @param onResetEmailChange Lambda called when the email input changes.
 * @param onResetEmailClick Lambda called when the "Send Reset Link" button is clicked.
 * @param onBackClick Lambda called when the "Back to Login" button is clicked.
 */
@Composable
fun ForgotPasswordScreenContent(
    email: String,
    errorMessage: String,
    successMessage: String,
    isLoading: Boolean,
    snackbarHost: SnackbarHostState,
    onResetEmailChange: (String) -> Unit,
    onResetEmailClick: () -> Unit,
    onBackClick: () -> Unit
){
    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHost) }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(LightOrange)
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 32.dp)
                    .align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(3.dp),
                    modifier = Modifier.fillMaxWidth().background(PureWhite),
                    colors = CardDefaults.cardColors(containerColor = PureWhite)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            "Having Trouble?",
                            style = MaterialTheme.typography.titleLarge,
                            textAlign = TextAlign.Center,
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            "Don’t sweat it, this happens quite often. We’re here to help, just enter your email address below and we’ll send you a reset link.",
                            style = MaterialTheme.typography.bodyLarge,
                            textAlign = TextAlign.Left,
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        OutlinedTextField(
                            value = email,
                            onValueChange = onResetEmailChange,
                            placeholder = { Text("Email") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                            shape = RoundedCornerShape(4.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Reset Button
                Button(
                    onClick = onResetEmailClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(30),
                    colors = ButtonDefaults.buttonColors(containerColor = Orange),
                    enabled = !isLoading
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = MaterialTheme.colorScheme.onPrimary,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text("Send Reset Link")
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Back Button
                OutlinedButton(
                    onClick = { onBackClick() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(30),
                ) {
                    Text("Back to Login")
                }
            }
        }
    }
}