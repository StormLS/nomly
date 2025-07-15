package com.example.nomlymealtracker.data.models

/**
 * Enum class representing the type of meal with the application.
 * Each enum constant has a display-friendly name used for UI rendering.
 * @property displayName: The user-friendly name of the meal type.
 */
enum class MealType(val displayName: String) {
    // Currently there are three main meal types: Breakfast, Lunch and Dinner (More could easy be added by adding to the enum)
    BREAKFAST("Breakfast"),
    LUNCH("Lunch"),
    DINNER("Dinner");

    /**
     * Attempts to convert a string into a [MealType], ignoring case.
     * If the input doesn't match any known [MealType], it returns [BREAKFAST] as the default fallback.
     * @param value The string to convert (e.g. "lunch", "DINNER").
     * @return The matching [MealType], or [BREAKFAST] if none match.
     */
    companion object {
        fun fromString(value: String): MealType? {
            return entries.firstOrNull { it.name.equals(value, ignoreCase = true) } ?: BREAKFAST
        }
    }
}