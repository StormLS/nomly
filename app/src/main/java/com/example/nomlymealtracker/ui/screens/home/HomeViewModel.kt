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

// Home Screen viewModel used to hold the screens data and UI states
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

    private fun loadMeals() {
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
