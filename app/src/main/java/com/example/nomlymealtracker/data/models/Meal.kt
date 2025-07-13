package com.example.nomlymealtracker.data.models

import com.google.firebase.Timestamp

data class Meal(
    val title: String = "",
    val description: String = "",
    val type: MealType = MealType.BREAKFAST,
    val calories: Double? = null,
    val protein: Double? = null,
    val carbs: Double? = null,
    val fats: Double? = null,
    val portionSize: String = "",
    val imageUrl: String? = null,
    val timestamp: Timestamp = Timestamp.now(),
    val id: String = "",
    val mealId: String = ""
)
