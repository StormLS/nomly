package com.example.nomlymealtracker.ui.screens.addMeal

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.nomlymealtracker.data.models.Meal
import com.example.nomlymealtracker.data.models.MealType
import com.google.firebase.Timestamp
import kotlinx.coroutines.delay

// The view model for the Add New Meal screen
class AddMealViewModel : ViewModel()
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

    suspend fun submitMeal(onSuccess: () -> Unit, onError: (String) -> Unit) {
        isSubmitting = true

        try {
            val meal = Meal(
                title = title.trim(),
                description = description.trim(),
                type = selectedMealType,
                portionSize = portionSize.trim(),
                protein = protein.toIntOrNull(),
                carbs = carbs.toIntOrNull(),
                fats = fats.toIntOrNull(),
                calories = calories.toIntOrNull(),
                timestamp = Timestamp.now()
            )

            //TODO: Put firebase logic here
            delay(3000) // simulate network call
            onSuccess()
            //TODO: Put firebase logic here


        } catch (e: Exception) {
            onError(e.message ?: "Unknown error")
        } finally {
            isSubmitting = false
        }
    }

    fun reset() {
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
}
