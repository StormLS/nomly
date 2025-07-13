package com.example.nomlymealtracker.ui.screens.viewMeal

import androidx.lifecycle.ViewModel
import com.example.nomlymealtracker.data.models.Meal
import com.example.nomlymealtracker.data.repository.ViewMealRepository
import kotlinx.coroutines.flow.Flow

class ViewMealViewModel(
    private val repository: ViewMealRepository = ViewMealRepository()
) : ViewModel(){
    fun getMeal(mealId: String): Flow<Meal?> {
        return repository.getMealById(mealId)
    }
}