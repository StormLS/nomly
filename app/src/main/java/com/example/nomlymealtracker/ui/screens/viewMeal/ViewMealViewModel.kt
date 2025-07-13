package com.example.nomlymealtracker.ui.screens.viewMeal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nomlymealtracker.data.models.Meal
import com.example.nomlymealtracker.data.repository.ViewMealRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ViewMealViewModel(
    private val repository: ViewMealRepository = ViewMealRepository()
) : ViewModel(){

    private val _meal = MutableStateFlow<Meal?>(null)
    val meal: StateFlow<Meal?> = _meal

    fun getMeal(mealId: String) {
        viewModelScope.launch {
            val result = repository.getMealById(mealId)
            _meal.value = result
        }
    }
}