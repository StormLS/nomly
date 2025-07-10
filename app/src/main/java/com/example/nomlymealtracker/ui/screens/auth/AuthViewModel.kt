package com.example.nomlymealtracker.ui.screens.auth

import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nomlymealtracker.data.models.AppUser
import com.example.nomlymealtracker.data.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import kotlinx.coroutines.launch

class AuthViewModel(
    private val authRepository: AuthRepository = AuthRepository()
) : ViewModel() {

    // Fields used by the Login Screen
    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)
    var loginSuccess by mutableStateOf(false)
    fun clearLoginError() {
        //email = ""
        //password = ""
        errorMessage = null
    }

    fun login() {
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            errorMessage = "Please enter a valid email address"
            return
        }
        if (password.length < 6) {
            errorMessage = "Password must be at least 6 characters"
            return
        }

        errorMessage = null
        isLoading = true

        viewModelScope.launch {
            val result = authRepository.login(email, password)
            isLoading = false
            result.onSuccess {
                loginSuccess = true
            }.onFailure {
                errorMessage = it.localizedMessage ?: "Login failed"
            }
        }
    }


    // Fields used by the Forgot Password Screen
    var resetEmail by mutableStateOf("")
    var resetLoading by mutableStateOf(false)
    var resetErrorMessage by mutableStateOf<String?>(null)
    var resetSuccess by mutableStateOf<String?>(null)
    fun clearResetError() {
        resetErrorMessage = null
    }
    fun clearResetSuccess() {
        resetSuccess = null
    }

    fun sendPasswordResetEmail() {
        resetErrorMessage = null
        resetSuccess = null

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(resetEmail).matches()) {
            resetErrorMessage = "Please enter a valid email address"
            return
        }

        resetLoading = true

        viewModelScope.launch {
            val result = authRepository.sendPasswordResetLink(resetEmail)
            resetLoading = false
            result.onSuccess {
                resetSuccess = "Password reset link sent to $resetEmail"
            }.onFailure {
                resetErrorMessage = it.localizedMessage ?: "Failed to send reset link"
            }
        }
    }

    // Fields used by the Register Screen
    var userName by mutableStateOf("")
    var userEmail by mutableStateOf("")
    var userPassword by mutableStateOf("")
    var userConfirmPassword by mutableStateOf("")

    var registerSuccess by mutableStateOf<String?>(null)
    var registerErrorMessage by mutableStateOf<String?>(null)
    var registerIsLoading by mutableStateOf(false)
    fun clearRegisterError() {
        registerErrorMessage = null
    }
    fun clearRegisterSuccess() {
        userName = ""
        userEmail = ""
        userPassword = ""
        userConfirmPassword = ""
        registerSuccess = null
    }

    fun resetStatus() {
        registerSuccess = null
        registerErrorMessage = null
        registerIsLoading = false
    }
    fun registerNewAccount() {
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()) {
            registerErrorMessage = "Please enter a valid email address"
            return
        }
        if (userPassword != userConfirmPassword) {
            registerErrorMessage = "Passwords do not match."
            return
        }
        if (userPassword.length < 6) {
            registerErrorMessage = "Password must be at least 6 characters."
            return
        }

        registerIsLoading = true
        registerErrorMessage = null

        viewModelScope.launch {
            val result = authRepository.registerNewAccount(userEmail.trim(), userPassword.trim())

            result.onSuccess { firebaseUser ->
                val appUser = AppUser(
                    fullName = userName.trim(),
                    email = userEmail.trim()
                )
                try {
                    authRepository.addToUserCollection(firebaseUser, appUser)
                    registerSuccess = "Account was successfully created"
                } catch (e: Exception) {
                    registerErrorMessage = "Registered, but failed to save user info: ${e.localizedMessage}"
                }
                registerIsLoading = false
            }.onFailure {
                registerIsLoading = false
                registerErrorMessage = when (it) {
                    is FirebaseAuthUserCollisionException -> "Email is already in use."
                    is FirebaseAuthWeakPasswordException -> "Password is too weak."
                    is FirebaseAuthInvalidCredentialsException -> "Invalid email address."
                    else -> it.localizedMessage ?: "Registration failed. Please try again."
                }
            }
        }
    }

}