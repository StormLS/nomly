package com.example.nomlymealtracker.data.repository

import com.example.nomlymealtracker.data.models.Meal
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class AddMealRepository {
    private val db = FirebaseFirestore.getInstance()

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