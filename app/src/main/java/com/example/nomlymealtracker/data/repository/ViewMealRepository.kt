package com.example.nomlymealtracker.data.repository

import com.example.nomlymealtracker.data.models.Meal
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class ViewMealRepository {
    private val db = FirebaseFirestore.getInstance()

    // Function used to fetch a single meal from  Firebase's Firestore database
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