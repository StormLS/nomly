package com.example.nomlymealtracker.data.repository

import com.example.nomlymealtracker.data.models.Meal
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class HomeRepository {
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    fun observeMeals(): Flow<List<Meal>> = callbackFlow {
        try {
            val userId = firebaseAuth.currentUser?.uid
            if (userId == null) {
                close(Exception("User not logged in"))
                return@callbackFlow
            }
            val listener: ListenerRegistration = db.collection("users")
                .document(userId)
                .collection("meals")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        close(error)
                        return@addSnapshotListener
                    }

                    val fetchedMeals = snapshot?.documents?.mapNotNull { doc ->
                        try {
                            doc.toObject(Meal::class.java)?.copy(id = doc.id)
                        } catch (e: Exception) {
                            null
                        }
                    }.orEmpty()

                    trySend(fetchedMeals)
                }
            awaitClose { listener.remove() }
        } catch (e: Exception) {
            throw e // Let the caller handle the error
        }
    }
}