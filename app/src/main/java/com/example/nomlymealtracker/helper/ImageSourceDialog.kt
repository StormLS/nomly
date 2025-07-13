package com.example.nomlymealtracker.helper

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
fun ImageSourceDialog(
    onDismiss: () -> Unit,
    onCameraClick: () -> Unit,
    onGalleryClick: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Select Image Source") },
        text = { Text("Choose image from:") },
        confirmButton = {
            TextButton(onClick = {
                onDismiss()
                onCameraClick()
            }) { Text("Camera") }
        },
        dismissButton = {
            TextButton(onClick = {
                onDismiss()
                onGalleryClick()
            }) { Text("Gallery") }
        }
    )
}
