package com.example.nomlymealtracker.ui.screens.viewMeal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nomlymealtracker.data.models.Meal
import com.example.nomlymealtracker.data.repository.ViewMealRepository
import com.example.nomlymealtracker.helper.Helper
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.IOException

/**
 * ViewModel responsible for fetching and holding the details of a Meal.
 *
 * @property repository An instance of [ViewMealRepository] used to retrieve meal data.
 */
class ViewMealViewModel(
    private val repository: ViewMealRepository = ViewMealRepository()
) : ViewModel(){

    private val _meal = MutableStateFlow<Meal?>(null)
    val meal: StateFlow<Meal?> = _meal

    /**
     * Asynchronously fetches a [Meal] by its ID from the repository and updates the state flow.
     *
     * @param mealId The unique identifier of the meal to be fetched.
     */
    fun getMeal(mealId: String) {
        viewModelScope.launch {
            val result = repository.getMealById(mealId)
            _meal.value = result
        }
    }

    fun deleteMeal(mealId: String) {
        viewModelScope.launch {
            repository.deleteMealById(mealId)
                .onSuccess { println("Meal Item Deleted") }
                .onFailure { e ->
                    when (e) {
                        is FirebaseFirestoreException -> println("Firebase Error")
                        is IOException -> println("IO Error")
                        else -> println("Generic Error")
                    }
                }
        }
    }
}