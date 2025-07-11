package com.example.nomlymealtracker.ui.screens.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.nomlymealtracker.R
import com.example.nomlymealtracker.ui.theme.LightOrange
import com.example.nomlymealtracker.ui.theme.MidOrange
import com.example.nomlymealtracker.ui.theme.NomlyMealTrackerTheme
import com.example.nomlymealtracker.ui.theme.Positive
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// Creating a Preview Friendly function for designing the Forgot Password Screen
@Preview
@Composable
fun RegisterScreenPreview() {
    NomlyMealTrackerTheme {
        RegisterScreenContent(
            name = "test",
            email = "dummy@gmail.com",
            password = "123456",
            confirmPassword = "123456",

            isLoading = false,
            snackbarHost = SnackbarHostState(),

            onNameChange = {},
            onEmailChange = {},
            onPasswordChange = {},
            onPasswordConfirmChange = {},
            onCreateAccountClick = {},
            onBackClick = {}
        )
    }
}

// The Composable that brings it all together
@Composable
fun RegisterScreen(
    navController: NavController,
    snackbarHost: SnackbarHostState,
    scope: CoroutineScope,
    viewModel: AuthViewModel = viewModel()
){
    val name = viewModel.userName
    val email = viewModel.userEmail
    val password = viewModel.userPassword
    val confirmPassword = viewModel.userConfirmPassword

    val errorMessage = viewModel.registerErrorMessage
    val successMessage = viewModel.registerSuccess
    val isLoading = viewModel.registerIsLoading

    LaunchedEffect(successMessage, errorMessage) {
        errorMessage?.let {
            scope.launch {
                viewModel.clearRegisterError()
                snackbarHost.showSnackbar(it, duration = SnackbarDuration.Short)
            }
        }
        successMessage?.let {
            scope.launch {
                viewModel.clearRegisterSuccess()
                snackbarHost.showSnackbar(it, duration = SnackbarDuration.Long)
            }
        }
    }

    RegisterScreenContent(
        name = name,
        email = email,
        password = password,
        confirmPassword = confirmPassword,

        isLoading = isLoading,
        snackbarHost = snackbarHost,

        onNameChange = { viewModel.userName = it },
        onEmailChange = { viewModel.userEmail = it },
        onPasswordChange = { viewModel.userPassword = it },
        onPasswordConfirmChange = { viewModel.userConfirmPassword = it },
        onCreateAccountClick = { viewModel.registerNewAccount() },
        onBackClick = { navController.popBackStack() }
    )
}

// The main composable for the Register Screen content
@Composable
fun RegisterScreenContent(
    name: String,
    email: String,
    password: String,
    confirmPassword: String,

    isLoading: Boolean,

    snackbarHost: SnackbarHostState,

    onNameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onPasswordConfirmChange: (String) -> Unit,

    onCreateAccountClick: () -> Unit,
    onBackClick: () -> Unit
){
    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHost) }
    ) { _ ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(LightOrange)
                .padding(24.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .weight(1f)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Creating a new Account",
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_email), // your PNG
                            contentDescription = "Email Icon",
                            modifier = Modifier.size(24.dp) // Set desired size here
                        )
                        Image(
                            painter = painterResource(id = R.drawable.ic_firebase), // your PNG
                            contentDescription = "Firebase Icon",
                            modifier = Modifier.size(24.dp) // Set desired size here
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "We currently only support email based accounts at this time.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Input Fields
                Text(text = "Account Name", style = MaterialTheme.typography.labelLarge)
                Spacer(modifier = Modifier.height(4.dp))
                TextField(
                    value = name,
                    onValueChange = onNameChange,
                    placeholder = { Text("Full Name") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = MidOrange,
                        focusedContainerColor = MidOrange
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(text = "Email Address", style = MaterialTheme.typography.labelLarge)
                Spacer(modifier = Modifier.height(4.dp))
                TextField(
                    value = email,
                    onValueChange = onEmailChange ,
                    placeholder = { Text("you@example.com") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = MidOrange,
                        focusedContainerColor = MidOrange
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(text = "Password", style = MaterialTheme.typography.labelLarge)
                Spacer(modifier = Modifier.height(4.dp))
                TextField(
                    value = password,
                    onValueChange = onPasswordChange,
                    placeholder = { Text("Enter Password") },
                    modifier = Modifier.fillMaxWidth(),
                    visualTransformation = PasswordVisualTransformation(),
                    singleLine = true,
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = MidOrange,
                        focusedContainerColor = MidOrange
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(text = "Confirm Password", style = MaterialTheme.typography.labelLarge)
                Spacer(modifier = Modifier.height(4.dp))
                TextField(
                    value = confirmPassword,
                    onValueChange = onPasswordConfirmChange,
                    placeholder = { Text("Repeat Password") },
                    modifier = Modifier.fillMaxWidth(),
                    visualTransformation = PasswordVisualTransformation(),
                    singleLine = true,
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = MidOrange,
                        focusedContainerColor = MidOrange
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Password must be at least 6 characters and contain a mix of letters and numbers.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }

            Column {
//            errorMessage.let {
//                Spacer(modifier = Modifier.height(8.dp))
//                Text(it, color = MaterialTheme.colorScheme.error)
//            }
//
//            successMessage.let {
//                Spacer(modifier = Modifier.height(8.dp))
//                Text(it, color = Positive) // green
//            }

                Button(
                    onClick = {
                        onCreateAccountClick()
                    },
                    enabled = !isLoading,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = MaterialTheme.colorScheme.onPrimary,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text("Register")
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedButton(
                    onClick = {
                        onBackClick()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Back to Login")
                }
            }
        }
    }

}