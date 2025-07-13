package com.example.nomlymealtracker.data.repository

import android.net.Uri
import com.example.nomlymealtracker.data.models.Meal
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class AddMealRepository {
    private val db = FirebaseFirestore.getInstance()

    suspend fun submitMeal(meal: Meal): Result<String> = withContext(Dispatchers.IO)
    {
        try {
            db.collection("meals")
                .add(meal)
                .await()

            Result.success("Meal submitted successfully")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun uploadMealImage(userId: String, imageUri: Uri): Result<String> = try
    {
        val filename = "meals/$userId/${System.currentTimeMillis()}.jpg"
        val storageRef = FirebaseStorage.getInstance().reference.child(filename)
        storageRef.putFile(imageUri).await()
        val downloadUrl = storageRef.downloadUrl.await().toString()
        Result.success(downloadUrl)
    } catch (e: Exception) {
        Result.failure(e)
    }
}