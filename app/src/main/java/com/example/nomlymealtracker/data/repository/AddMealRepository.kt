package com.example.nomlymealtracker.data.repository

import com.example.nomlymealtracker.data.models.Meal
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

/**
 * Repository class responsible for handling meal-related operations with Firestore.
 * This class abstracts Firestore write operations from the ViewModel, keeping data logic separate from UI logic.
 */
class AddMealRepository {
    private val db = FirebaseFirestore.getInstance()

    /**
     * Submits a [Meal] object to the "meals" collection in Firestore.
     * @param meal The [Meal] data to be stored.
     * @return A [Result] containing a success message if the operation succeeds, or a failure with an exception if it fails.
     * This function runs on the IO dispatcher to avoid blocking the main thread.
     */
    suspend fun submitMeal(meal: Meal): Result<String> = withContext(Dispatchers.IO)
    {
        try {
            db.collection("meals")
                .add(meal)
                .await()

            Result.success("Meal submitted successfully")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}