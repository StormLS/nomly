package com.example.nomlymealtracker.data.repository

import com.example.nomlymealtracker.data.models.Meal
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class ViewMealRepository {
    private val db = FirebaseFirestore.getInstance()

    fun getMealById(mealId: String): Flow<Meal?> = callbackFlow {
        val listener = db.collection("meals")
            .document(mealId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                val meal = snapshot?.toObject(Meal::class.java)
                trySend(meal)
            }

        awaitClose { listener.remove() }
    }
}