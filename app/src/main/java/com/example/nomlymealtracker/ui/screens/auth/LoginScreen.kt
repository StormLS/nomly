package com.example.nomlymealtracker.ui.screens.auth

import androidx.collection.emptyLongSet
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.nomlymealtracker.R
import com.example.nomlymealtracker.ui.theme.NomlyMealTrackerTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * Creating a Preview Friendly function for designing the Login Screen
 */
@Preview()
@Composable
fun LoginScreenPreview() {
    NomlyMealTrackerTheme {
        LoginScreenContent(
            email = "test@example.com",
            password = "password",
            isLoading = false,
            snackbarHost = SnackbarHostState(),
            onEmailChange = {},
            onPasswordChange = {},
            onLoginClick = {},
            onRegisterClick = {},
            onForgotPasswordClick = {}
        )
    }
}

/**
 * Composable function that displays the Login screen and handles user interaction for logging in.
 * @param snackbarHost The SnackbarHostState used to show snackbars for error messages.
 * @param scope CoroutineScope for launching coroutines, e.g., to show snackbars.
 * @param viewModel The AuthViewModel instance managing UI state and login logic.
 * @param onLoginSuccess Callback invoked when login is successful, typically to navigate to the next screen.
 * @param onRegisterClick Callback invoked when the user taps the Register button.
 * @param onForgotPasswordClick Callback invoked when the user taps the Forgot Password button.
 */
@Composable
fun LoginScreen(
    snackbarHost: SnackbarHostState,
    scope: CoroutineScope,
    viewModel: AuthViewModel = viewModel(),
    onLoginSuccess: () -> Unit,
    onRegisterClick: () -> Unit,
    onForgotPasswordClick: () -> Unit
) {
    val email = viewModel.email
    val password = viewModel.password
    val isLoading = viewModel.isLoading
    val errorMessage = viewModel.errorMessage
    val loginSuccess = viewModel.loginSuccess

    LaunchedEffect(loginSuccess, errorMessage) {
        if (loginSuccess) {
            onLoginSuccess()
        }
        errorMessage?.let {
            scope.launch {
                viewModel.clearLoginError()
                snackbarHost.showSnackbar(it, duration = SnackbarDuration.Short)
            }
        }
    }

    LoginScreenContent(
        email = email,
        password = password,

        isLoading = isLoading,
        snackbarHost = snackbarHost,

        onEmailChange = { viewModel.email = it },
        onPasswordChange = { viewModel.password = it },
        onForgotPasswordClick = { onForgotPasswordClick() },
        onRegisterClick = { onRegisterClick() },
        onLoginClick = { viewModel.login() }
    )
}

/**
 * The main UI content composable for the Login screen.
 * Displays input fields for email and password, buttons for login, registration, and navigation to forgot password.
 * Also handles loading state and shows a progress indicator on the login button when loading.
 * @param email Current text input for the email field.
 * @param password Current text input for the password field.
 * @param isLoading Boolean flag indicating whether a login request is in progress.
 * @param snackbarHost SnackbarHostState to show snackbars (though not directly used here, passed for scaffold).
 * @param onEmailChange Lambda called when the email input changes.
 * @param onPasswordChange Lambda called when the password input changes.
 * @param onForgotPasswordClick Lambda called when the user taps the "Forgot Password?" button.
 * @param onRegisterClick Lambda called when the user taps the "Register" button.
 * @param onLoginClick Lambda called when the user taps the "Login" button.
 */
@Composable
fun LoginScreenContent(
    email: String,
    password: String,

    isLoading: Boolean,
    snackbarHost: SnackbarHostState,

    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onForgotPasswordClick: () -> Unit,
    onRegisterClick: () -> Unit,
    onLoginClick: () -> Unit
) {
    val scrollState = rememberScrollState()
    var passwordVisible by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHost)
    }) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(scrollState)
                    .padding(horizontal = 16.dp)
                    .imePadding(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(24.dp))

                Image(
                    painter = painterResource(id = R.drawable.nomly_logo),
                    contentDescription = "Logo",
                    modifier = Modifier
                        .size(180.dp)
                )

                // Will represent the input field for the User Email
                OutlinedTextField(
                    value = email,
                    onValueChange = onEmailChange,
                    label = { Text("Email") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Will represent the input field for the User Password
                OutlinedTextField(
                    value = password,
                    onValueChange = onPasswordChange,
                    label = { Text("Password") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    singleLine = true,
                    trailingIcon = {
                        val image = if (passwordVisible)
                            Icons.Default.VisibilityOff
                        else
                            Icons.Default.Visibility

                        IconButton(onClick = {passwordVisible = !passwordVisible}) {
                            Icon(imageVector = image, contentDescription = if (passwordVisible) "Hide Password" else "Show Password")
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                // Go to the Forgot Password screen
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onForgotPasswordClick) {
                        Text("Forgot Password?", color = Color.Black, fontSize = 14.sp)
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Button(
                        onClick = onRegisterClick,
                        modifier = Modifier.weight(1f).height(52.dp),
                        shape = RoundedCornerShape(30),
                    ) {
                        Text("Register")
                    }

                    Button(
                        onClick = onLoginClick,
                        enabled = !isLoading,
                        modifier = Modifier.weight(1f).height(52.dp),
                        shape = RoundedCornerShape(30),
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = MaterialTheme.colorScheme.onPrimary,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text("Login")
                        }
                    }
                }

            }
        }
    }
}