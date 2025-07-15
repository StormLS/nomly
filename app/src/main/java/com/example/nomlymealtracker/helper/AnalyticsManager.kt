package com.example.nomlymealtracker.helper

import android.os.Bundle
import com.google.firebase.Firebase
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.analytics

/**
 * Singleton object responsible for logging analytics events to Firebase Analytics.
 */
object AnalyticsManager {
    /**
     * Logs a screen view event to Firebase Analytics.
     * @param screenName The name of the screen being viewed.
     */
    fun logScreen(screenName: String) {
        Firebase.analytics.logEvent("screen_view", Bundle().apply {
            putString(FirebaseAnalytics.Param.SCREEN_NAME, screenName)
        })
    }

    /**
     * Logs an event indicating that a meal was shared.
     * @param mealId The unique identifier of the shared meal.
     */
    fun logMealShared(mealId: String) {
        Firebase.analytics.logEvent("meal_shared", Bundle().apply {
            putString("meal_id", mealId)
        })
    }

    /**
     * Logs an event when a meal is tracked (added by the user).
     * @param mealType The type of meal tracked by the user (e.g., "BREAKFAST", "LUNCH", "DINNER").
     */
    fun logMealTracked(mealType: String) {
        Firebase.analytics.logEvent("meal_tracked", Bundle().apply {
            putString("meal_type", mealType)
        })
    }
}