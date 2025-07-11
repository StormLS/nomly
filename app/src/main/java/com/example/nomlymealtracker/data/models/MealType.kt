package com.example.nomlymealtracker.data.models

enum class MealType(val displayName: String) {
    BREAKFAST("Breakfast"),
    LUNCH("Lunch"),
    DINNER("Dinner");

    companion object {
        fun fromString(value: String): MealType? {
            return entries.firstOrNull { it.name.equals(value, ignoreCase = true) } ?: BREAKFAST
        }
    }
}