package com.example.nomlymealtracker.ui.screens.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.nomlymealtracker.data.models.Meal
import com.example.nomlymealtracker.data.repository.HomeRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class HomeViewModel(
    private val homeRepository: HomeRepository = HomeRepository()
) : ViewModel() {
    /*var meals = mutableStateListOf<Meal>()
    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)

    init {
        viewModelScope.launch {
            isLoading = true
            dashboardRepository.observeMeals().collect { fetchedMeals ->
                isLoading = false
                meals.clear()
                meals.addAll(fetchedMeals)
            }
        }
    }
     */

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    var meals = mutableStateListOf<Meal>()
    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)

    init {
        fetchMeals()
    }

    private fun fetchMeals() {
        val userId = auth.currentUser?.uid ?: return
        println("Finding meals for $userId")
        isLoading = true
        db.collection("users")
            .document(userId)
            .collection("meals")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->

                isLoading = false
                if (error != null) {
                    errorMessage = error.localizedMessage
                    return@addSnapshotListener
                }
                println("Was Successful:\n${snapshot!!.documents}")

                val fetchedMeals = snapshot?.documents?.mapNotNull { doc ->
                    println("Fetch Meals:\n${doc}")
                    try {
                        val meal = doc.toObject(Meal::class.java)?.copy(id = doc.id)
                        meal
                    } catch (e: Exception) {
                        println("Snapshot error:\n${e}")
                        errorMessage = e.localizedMessage
                        null
                    }
                }.orEmpty()

                meals.clear()
                meals.addAll(fetchedMeals)
            }
    }
}