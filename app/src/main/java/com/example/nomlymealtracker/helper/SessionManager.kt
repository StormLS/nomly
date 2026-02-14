package com.example.nomlymealtracker.helper

import android.content.Context
import android.content.SharedPreferences

/**
 * Singleton object responsible for managing the user's session preferences,
 * specifically the "Remember Me" functionality using SharedPreferences.
 */
object SessionManager {

    private const val PREFS_NAME = "nomly_session_prefs"
    private const val KEY_REMEMBER_ME = "remember_me"

    private fun getPrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    /**
     * Sets the "Remember Me" preference.
     * @param context The application or activity context.
     * @param rememberMe Whether the user wants to stay logged in across app launches.
     */
    fun setRememberMe(context: Context, rememberMe: Boolean) {
        getPrefs(context).edit().putBoolean(KEY_REMEMBER_ME, rememberMe).apply()
    }

    /**
     * Reads the "Remember Me" preference.
     * @param context The application or activity context.
     * @return True if the user previously opted to be remembered, false otherwise.
     */
    fun isRememberMeEnabled(context: Context): Boolean {
        return getPrefs(context).getBoolean(KEY_REMEMBER_ME, false)
    }

    /**
     * Clears all session preferences. Called on logout.
     * @param context The application or activity context.
     */
    fun clearSession(context: Context) {
        getPrefs(context).edit().clear().apply()
    }
}
