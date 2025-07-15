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

/**
 * A utility object containing reusable helper functions used throughout the app.
 * Functions include time formatting, meal sharing, and image encoding/decoding.
 */
object Helper
{
    /**
     * Formats a [Timestamp] into a string of format `"dd MMM yyyy, HH:mm"`.
     * Example: `05 Jul 2025, 14:30`
     * @param timestamp The [Timestamp] to format.
     * @return A formatted date-time string.
     */
    fun formatTimestamp(timestamp: Timestamp): String {
        val date = timestamp.toDate()
        val formatter = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())
        return formatter.format(date)
    }

    /**
     * Converts a [Timestamp] to a human-readable relative time (e.g., "5 minutes ago").
     * @param timestamp The [Timestamp] to convert.
     * @return A relative time string.
     */
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

    /**
     * Shares the details of a [Meal] with other apps via Android's share sheet.
     * @param context The current [Context].
     * @param meal The [Meal] to share.
     */
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

    /**
     * Encodes an image from a [Uri] into a Base64 string for storage in Firestore.
     * @param uri The [Uri] of the image to encode.
     * @param context The current [Context] to access the content resolver.
     * @return A Base64-encoded string representation of the image.
     * @throws IllegalArgumentException if the input stream is null or if the image is too large.
     */
    fun encodeImageToBase64(uri: Uri, context: Context): String {
        val inputStream = context.contentResolver.openInputStream(uri) ?: throw IllegalArgumentException("Can't open input stream for URI")

        val bytes = inputStream.readBytes()
        inputStream.close()

        if (bytes.size > 1_000_000) { // Optional: 1 MB size check
            throw IllegalArgumentException("Image is too large. Max 1MB allowed.")
        }

        return Base64.encodeToString(bytes, Base64.NO_WRAP)
    }

    /**
     * Decodes a Base64-encoded image string into an [ImageBitmap] for display in Jetpack Compose.
     * @param base64String The Base64 string to decode.
     * @return The decoded [ImageBitmap], or `null` if decoding fails.
     */
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