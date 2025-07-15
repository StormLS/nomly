package com.example.nomlymealtracker.data.repository

import com.example.nomlymealtracker.data.models.Meal
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

/**
 * Repository class responsible for retrieving a single meal
 * from Firestore based on its [Meal.mealId].
 * This class abstracts Firestore write operations from the ViewModel, keeping data logic separate from UI logic.
 */
class ViewMealRepository {
    private val db = FirebaseFirestore.getInstance()

    /**
     * Fetches a specific [Meal] from the "meals" collection using its Firestore document ID.
     * @param mealId The Firestore document ID of the meal.
     * @return A [Meal] object if found, or `null` if not found or if an error occurs.
     * The returned [Meal] will have its [Meal.mealId] field set to the Firestore document ID.
     */
    suspend fun getMealById(mealId: String): Meal? = withContext(Dispatchers.IO) {
        try {
            val snapshot = db.collection("meals")
                .document(mealId)
                .get()
                .await()

            snapshot.toObject(Meal::class.java)?.copy(mealId = snapshot.id)
        } catch (e: Exception) {
            println("FirebaseFetch - Error getting meal - $e")
            null
        }
    }
}