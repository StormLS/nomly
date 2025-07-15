package com.example.nomlymealtracker.data.repository

import com.example.nomlymealtracker.data.models.Meal
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

/**
 * Repository class responsible for interacting with Firebase Firestore
 * to retrieve meal data for the authenticated user.
 * This class handles all network-related operations for the Home screen.
 * This class abstracts Firestore write operations from the ViewModel, keeping data logic separate from UI logic.
 */
class HomeRepository
{
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    /**
     * Fetches a list of meals from the "meals" Firestore collection,
     * filtered by the currently authenticated users ID and sorted by most recent.
     * @return A list of [Meal] objects associated with the current user, sorted by descending timestamp.
     * If no user is authenticated, an empty list is returned.
     * In case of an error, the exception is logged and an empty list is returned.
     */
    suspend fun fetchMeals(): List<Meal> = withContext(Dispatchers.IO)
    {
        val userId = firebaseAuth.currentUser?.uid
        val mealList = mutableListOf<Meal>()

        // I filter by UserId and put it in descending order back on when it was created
        try {
            val snapshot = db.collection("meals")
                .whereEqualTo("id", userId)
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .get()
                .await()

            for (doc in snapshot.documents) {
                val meal = doc.toObject(Meal::class.java)
                if (meal != null) {
                    mealList.add(meal.copy(mealId = doc.id))
                }
            }
        } catch (e: Exception) {
            println("FirebaseFetch - Error getting meals - $e")
        }

        return@withContext mealList
    }
}