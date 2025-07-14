package com.example.nomlymealtracker.helper

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.text.format.DateUtils
import android.util.Base64
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
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
            appendLine("Check out this meal!\n")
            appendLine("Title: ${meal.title}")
            appendLine("Description: ${meal.description}")
            appendLine("Type: ${meal.type}")
            appendLine("Portion Size: ${meal.portionSize}")
            appendLine("Protein: ${meal.protein?.toString() ?: "0"}g")
            appendLine("Carbs: ${meal.carbs?.toString() ?: "0"}g")
            appendLine("Fats: ${meal.fats?.toString() ?: "0"}g")
            appendLine("Calories: ${meal.calories?.toString() ?: "N/A"}")
            appendLine("Consumed at: ${ formatTimestamp(meal.timestamp) }")
        }

        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, shareText)
        }

        val chooser = Intent.createChooser(intent, "Share Meal With ")
        context.startActivity(chooser)
    }

    fun encodeImageToBase64(uri: Uri, context: Context): String {
        val inputStream = context.contentResolver.openInputStream(uri) ?: throw IllegalArgumentException("Can't open input stream for URI")

        val bytes = inputStream.readBytes()
        inputStream.close()

        if (bytes.size > 1_000_000) { // Optional: 1 MB size check
            throw IllegalArgumentException("Image is too large. Max 1MB allowed.")
        }

        return Base64.encodeToString(bytes, Base64.NO_WRAP)
    }

    fun decodeBase64ToImageBitmap(base64String: String): ImageBitmap? {
        return try {
            val decodedBytes = Base64.decode(base64String, Base64.DEFAULT)
            val bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
            bitmap?.asImageBitmap()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}