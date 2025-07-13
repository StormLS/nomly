package com.example.nomlymealtracker.helper

import android.content.Context
import android.content.Intent
import android.text.format.DateUtils
import com.example.nomlymealtracker.data.models.Meal
import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.Locale

// A Helper object where I can put anything I need to help me throughout the app
object Helper
{
    // A function used to help me convert a Timestamp to a formatted string, in this case "dd MMM yyyy, HH:mm"
    fun formatTimestamp(timestamp: Timestamp): String {
        val date = timestamp.toDate()
        val formatter = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())
        return formatter.format(date)
    }

    // A function used to help me get a relative time string from a Timestamp
    fun getRelativeTime(timestamp: Timestamp): String {
        val now = System.currentTimeMillis()
        val time = timestamp.toDate().time
        return DateUtils.getRelativeTimeSpanString(
            time,
            now,
            DateUtils.MINUTE_IN_MILLIS,
            DateUtils.FORMAT_ABBREV_RELATIVE
        ).toString()
    }

    // A function used to share meal details with someone on another app
    fun shareMeal(context: Context, meal: Meal) {
        val shareText = buildString {
            appendLine("Check out this meal!")
            appendLine("Title: ${meal.title}")
            appendLine("Description: ${meal.description}")
            appendLine("Type: ${meal.type}")
            appendLine("Portion Size: ${meal.portionSize}")
            appendLine("Protein: ${meal.protein?.toString() ?: "0"}g")
            appendLine("Carbs: ${meal.carbs?.toString() ?: "0"}g")
            appendLine("Fats: ${meal.fats?.toString() ?: "0"}g")
            appendLine("Calories: ${meal.calories?.toString() ?: "N/A"}")
            appendLine("Consumed at: ${meal.timestamp.toDate()}")
        }

        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, shareText)
        }

        val chooser = Intent.createChooser(intent, "Share Meal With ")
        context.startActivity(chooser)
    }

}