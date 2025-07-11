package com.example.nomlymealtracker.data.models

import com.google.firebase.Timestamp

data class Meal(
    val title: String = "",
    val description: String = "",
    val type: MealType = MealType.BREAKFAST,
    val calories: Int? = null,
    val protein: Int? = null,
    val carbs: Int? = null,
    val fats: Int? = null,
    val portionSize: String = "",
    val imageUrl: String? = null,
    val timestamp: Timestamp = Timestamp.now(),
    val id: String = ""
)
