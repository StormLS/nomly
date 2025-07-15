package com.example.nomlymealtracker.data.repository

import com.example.nomlymealtracker.data.models.AppUser
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

/**
 * Repository responsible for handling User Authentication and account-related operations such as User Registration and Forgot Password
 * This is done using Firebase Authentication and Firestore.
 * This class abstracts Firestore write operations from the ViewModel, keeping data logic separate from UI logic.
 */
class AuthRepository
{
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    /**
     * Attempts to log in the user with the provided email and password.
     * @param email The users email address.
     * @param password The users password.
     * @return A [Result] containing the [FirebaseUser] on success, or an exception on failure.
     */
    suspend fun login(email: String, password: String): Result<FirebaseUser> {
        return try {
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            Result.success(result.user!!)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Sends a password reset email to the specified email address.
     * @param email The users email address.
     * @return A [Result] indicating success or containing an exception on failure.
     */
    suspend fun sendPasswordResetLink(email: String): Result<Unit> {
        return try {
            firebaseAuth.sendPasswordResetEmail(email).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Registers a new account using the given email and password.
     * @param email The new users email address.
     * @param password The new user's chosen password.
     * @return A [Result] containing the [FirebaseUser] on success, or an exception on failure.
     */
    suspend fun registerNewAccount(email: String, password: String): Result<FirebaseUser> {
        return try {
            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            Result.success(result.user!!)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Adds the authenticated user's profile data in the Firestore "users" collection.
     * @param user The authenticated [FirebaseUser].
     * @param appUser The application-specific user data to store.
     * This method does not return a [Result] — it throws on failure and should be caught by the caller.
     */
    suspend fun addToUserCollection(user: FirebaseUser, appUser: AppUser){
        try {
            db.collection("users")
                .document(user.uid)
                .set(appUser)
                .await() // suspend until task completes
        } catch (e: Exception) {
            throw e // Let the caller handle the error
        }
    }
}