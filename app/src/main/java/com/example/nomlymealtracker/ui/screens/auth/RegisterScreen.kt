package com.example.nomlymealtracker.ui.screens.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.nomlymealtracker.R
import com.example.nomlymealtracker.helper.TextFieldWithLabel
import com.example.nomlymealtracker.ui.theme.LightOrange
import com.example.nomlymealtracker.ui.theme.NomlyMealTrackerTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * Creating a Preview Friendly function for designing the Forgot Password Screen
 */
@Preview
@Composable
fun RegisterScreenPreview() {
    NomlyMealTrackerTheme {
        RegisterScreenContent(
            name = "Storm Stuebchen",
            email = "lstuebchen@gmail.com",
            password = "superStrongPassword123",
            confirmPassword = "superStrongPassword123",

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

/**
 * Composable function that displays the Register screen and handles user registration flow.
 * @param navController Navigation controller used to navigate between screens.
 * @param snackbarHost SnackbarHostState used to display error or success messages.
 * @param coroutineScope CoroutineScope used to launch coroutines for snackbar display.
 * @param viewModel AuthViewModel that manages registration UI state and business logic.
 */
@Composable
fun RegisterScreen(
    navController: NavController,
    snackbarHost: SnackbarHostState,
    coroutineScope: CoroutineScope,
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
            coroutineScope.launch {
                viewModel.clearRegisterError()
                snackbarHost.showSnackbar(it, duration = SnackbarDuration.Short)
            }
        }
        successMessage?.let {
            coroutineScope.launch {
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

/**
 * The main composable for displaying the registration screen UI.
 * This composable provides input fields for account name, email, password, and password confirmation, along with Register and Back buttons.
 * It also displays a loading indicator during registration and shows snackbars for messages via the provided [snackbarHost].
 * @param name The current value of the user's account name input.
 * @param email The current value of the user's email input.
 * @param password The current value of the user's password input.
 * @param confirmPassword The current value of the password confirmation input.
 * @param isLoading Whether the registration process is in progress.
 * @param snackbarHost The SnackbarHostState used to display snackbars.
 * @param onNameChange Callback invoked when the account name input changes.
 * @param onEmailChange Callback invoked when the email input changes.
 * @param onPasswordChange Callback invoked when the password input changes.
 * @param onPasswordConfirmChange Callback invoked when the confirm password input changes.
 * @param onCreateAccountClick Callback invoked when the Register button is clicked.
 * @param onBackClick Callback invoked when the Back to Login button is clicked.
 */
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
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(LightOrange)
                .padding(innerPadding)
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

                // User Input field for the Account Name
                TextFieldWithLabel("Account Name", name, onNameChange)

                Spacer(modifier = Modifier.height(16.dp))

                // User Input field for the Email Address
                TextFieldWithLabel("Email Address", email, onEmailChange)

                Spacer(modifier = Modifier.height(16.dp))

                // User Input field for the Password
                TextFieldWithLabel("Password", password, onPasswordChange, isPassword = true)

                Spacer(modifier = Modifier.height(16.dp))

                // User Input field for the Password
                TextFieldWithLabel("Confirm Password", confirmPassword, onPasswordConfirmChange, isPassword = true)

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Password must be at least 6 characters and include uppercase, lowercase, a number, and a special character.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }

            Column {
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