package com.example.nomlymealtracker.ui.screens.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.nomlymealtracker.R
import com.example.nomlymealtracker.ui.theme.NomlyMealTrackerTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

// Creating a Preview Friendly function for designing the Login Screen
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

// The Composable that brings it all together
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

// The main composable for the Login Screen content
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
    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHost)
    }) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(Color(0xFFFFFBF7)) // Soft off-white background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.nomly_logo),
                    contentDescription = "Logo",
                    modifier = Modifier
                        .size(180.dp)
                )

                Text("Login", fontSize = 22.sp, fontWeight = FontWeight.Bold)

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
                    visualTransformation = PasswordVisualTransformation(),
                    singleLine = true,
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
