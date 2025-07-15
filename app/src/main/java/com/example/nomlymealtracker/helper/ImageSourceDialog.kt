package com.example.nomlymealtracker.helper

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

/**
 * A simple composable dialog that allows the user to choose between taking a photo with the camera or selecting one from the gallery.
 * @param onDismiss Called when the dialog is dismissed.
 * @param onCameraClick Called when the user selects the "Camera" option.
 * @param onGalleryClick Called when the user selects the "Gallery" option.
 */
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