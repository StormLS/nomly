package com.example.nomlymealtracker.data.repository

import com.example.nomlymealtracker.data.models.Meal
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

// Home Repository class that is responsible for doing all of the Firebase Network called to retrieve data, send data, etc
class HomeRepository
{
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    // Function used to fetch the meals from Firebase's Firestore database
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