package com.example.nomlymealtracker.ui.screens.addMeal

import android.app.Application
import android.content.Context
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nomlymealtracker.data.models.Meal
import com.example.nomlymealtracker.data.models.MealType
import com.example.nomlymealtracker.data.repository.AddMealRepository
import com.example.nomlymealtracker.helper.Helper.encodeImageToBase64
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
    var imageUri by mutableStateOf<Uri?>(null)

    var errorMessage by mutableStateOf<String?>(null)
    var successMessage by mutableStateOf<String?>(null)

    var isSubmitting by mutableStateOf(false)

    fun submitMeal(context: Context) {
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
                isSubmitting = false
                return
            }

            viewModelScope.launch {
                var imageBase64: String? = null
                if (imageUri != null) {
                    try {
                        imageBase64 = encodeImageToBase64(imageUri!!, context = context)
                    } catch (e: Exception) {
                        errorMessage = "Image too large or unreadable"
                        imageBase64 = null
                    }
                }

                val meal = Meal(
                    title = title.trim(),
                    description = description.trim(),
                    type = selectedMealType,
                    calories = calories.toDoubleOrNull(),
                    protein = protein.toDoubleOrNull(),
                    carbs = carbs.toDoubleOrNull(),
                    fats = fats.toDoubleOrNull(),
                    portionSize = portionSize.trim(),
                    imageBase64 = imageBase64,
                    timeEaten = timeOfConsumption.trim(),
                    timestamp = Timestamp.now(),
                    id = userId
                )

                println("Meal to be added: $meal")

                val result = addMealRepository.submitMeal(meal)
                if (result.isSuccess) {
                    val message = result.getOrNull()
                    successMessage = message
                    reset()
                } else {
                    val errorMesg = result.exceptionOrNull()?.message
                    errorMessage = errorMesg
                }
            }
        } catch (e: Exception) {
            reset()
            errorMessage = "Failed to add the meal - $e"
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
        imageUri = null
        isSubmitting = false
    }

    private fun validateInputs(): String? {
        return when {
            title.trim().isEmpty() -> "Title is required"
            title.length > 100 -> "Title must be 100 characters or less"
            description.trim().isEmpty() -> "Description is required"
            description.length > 300 -> "Description must be 300 characters or less"
            timeOfConsumption.trim().isEmpty() -> "Time of consumption is required"
            portionSize.trim().isEmpty() -> "Portion size is required"
            else -> null // No validation errors
        }
    }
}
