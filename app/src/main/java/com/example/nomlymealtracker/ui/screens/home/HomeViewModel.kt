package com.example.nomlymealtracker.ui.screens.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nomlymealtracker.data.models.Meal
import com.example.nomlymealtracker.data.repository.HomeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel responsible for managing the state and data of the Home Screen.
 *
 * Holds the list of meals, search term, and loading state.
 * Fetches meals from the [HomeRepository] and exposes them as a [StateFlow].
 *
 * @property homeRepository The repository used to fetch meals data.
 */
class HomeViewModel(
    private val homeRepository: HomeRepository = HomeRepository()
) : ViewModel() {

    var searchTerm by mutableStateOf("")

    private val _meals = MutableStateFlow<List<Meal>>(emptyList())
    val meals: StateFlow<List<Meal>> = _meals

    var mealsIsLoading by mutableStateOf(false)

    init {
        loadMeals()
    }

    fun loadMeals() {
        viewModelScope.launch {
            try {
                mealsIsLoading = true
                _meals.value = homeRepository.fetchMeals()
            } catch (e: Exception) {
                println("Failed to fetch meals - $e")
            } finally {
                mealsIsLoading = false
            }
        }
    }
}