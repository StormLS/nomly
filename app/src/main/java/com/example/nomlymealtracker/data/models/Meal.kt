package com.example.nomlymealtracker.data.models

import com.google.firebase.Timestamp

/**
 * Data class used to represent a Meal in the App
 * @property title: The title of the meal eg. "Chicken Burger"
 * @property description: A description of the meal eg. "A juicy burger with all the fixings"
 * @property type: Enum used for Meal Types, eg. Breakfast, Lunch, Dinner. More could be added (Snack, Water)
 * @property calories: Calories in the meal
 * @property protein: Protein in the meal
 * @property carbs: Carbs in the meal
 * @property fats: Fats in the meal
 * @property portionSize: The portion size of the meal eg. "1 portion", "1x". This is up to the user
 * @property imageBase64: Base64 encoded image of the meal to be send to Firestore
 * @property timeEaten: The time the meal was eaten by the user. String for high flexibility of input eg. "12:30 AM"
 * @property timestamp: Timestamp of when the meal was captured
 * @property id: The ID of the user who captured it
 * @property mealId: The ID of the meal
 */
data class Meal(
    val title: String = "",
    val description: String = "",
    val type: MealType = MealType.BREAKFAST,
    val calories: Double? = null,
    val protein: Double? = null,
    val carbs: Double? = null,
    val fats: Double? = null,
    val portionSize: String = "",
    val imageBase64: String? = null,
    val timeEaten: String = "",
    val timestamp: Timestamp = Timestamp.now(),
    val id: String = "",
    val mealId: String = ""
)