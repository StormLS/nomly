package com.example.nomlymealtracker.ui.screens.addMeal

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nomlymealtracker.data.models.Meal
import com.example.nomlymealtracker.data.models.MealType
import com.example.nomlymealtracker.data.repository.AddMealRepository
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

// The view model for the Add New Meal screen
class AddMealViewModel(
    private val addMealRepository: AddMealRepository = AddMealRepository()
) : ViewModel()
{
    // Core user input fields
    var title by mutableStateOf("")
    var description by mutableStateOf("")
    var timeOfConsumption by mutableStateOf("")
    var selectedMealType by mutableStateOf(MealType.BREAKFAST)
    var portionSize by mutableStateOf("")
    var protein by mutableStateOf("")
    var carbs by mutableStateOf("")
    var fats by mutableStateOf("")
    var calories by mutableStateOf("")

    var errorMessage by mutableStateOf<String?>(null)
    var successMessage by mutableStateOf<String?>(null)

    var isSubmitting by mutableStateOf(false)

    fun submitMeal() {
        val error = validateInputs()
        if (error != null) {
            viewModelScope.launch {
                errorMessage = error
            }
            return
        }

        isSubmitting = true

        try {
            val userId = FirebaseAuth.getInstance().currentUser?.uid
            if (userId == null) {
                errorMessage = "User not authenticated"
                return
            }

            val meal = Meal(
                title = title.trim(),
                description = description.trim(),
                type = selectedMealType,
                portionSize = portionSize.trim(),
                protein = protein.toDoubleOrNull(),
                carbs = carbs.toDoubleOrNull(),
                fats = fats.toDoubleOrNull(),
                calories = calories.toDoubleOrNull(),
                timestamp = Timestamp.now(),
                id = userId
            )

            viewModelScope.launch {
                val result = addMealRepository.submitMeal(meal)
                if (result.isSuccess) {
                    val message = result.getOrNull()
                    successMessage = message
                    reset()
                } else {
                    val error = result.exceptionOrNull()?.message
                    errorMessage = error
                }
            }
        } catch (e: Exception) {
            errorMessage = "Failed to add the meal - $e"
        } finally {
            isSubmitting = false
        }
    }

    private fun reset() {
        title = ""
        description = ""
        timeOfConsumption = ""
        selectedMealType = MealType.BREAKFAST
        portionSize = ""
        protein = ""
        carbs = ""
        fats = ""
        calories = ""
        isSubmitting = false
    }

    private fun validateInputs(): String? {
        return when {
            title.trim().isEmpty() -> "Title is required"
            description.trim().isEmpty() -> "Description is required"
            timeOfConsumption.trim().isEmpty() -> "Time of consumption is required"
            portionSize.trim().isEmpty() -> "Portion size is required"
            else -> null // No validation errors
        }
    }
}
