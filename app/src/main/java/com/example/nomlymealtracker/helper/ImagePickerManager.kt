package com.example.nomlymealtracker.helper

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.compose.ManagedActivityResultLauncher
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted

class ImagePickerManager @OptIn(ExperimentalPermissionsApi::class) constructor(
    private val context: Context,
    private val cameraPermissionState: PermissionState,
    private val onImagePicked: (Uri) -> Unit
) {
    private var cameraImageUri: Uri? = null

    fun createImageUri(): Uri {
        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "meal_${System.currentTimeMillis()}.jpg")
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/Nomly")
        }

        return context.contentResolver.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            contentValues
        ) ?: throw IllegalStateException("Failed to create MediaStore entry")
    }

    fun handleGalleryResult(uri: Uri?) {
        uri?.let { onImagePicked(it) }
    }

    fun handleCameraResult(success: Boolean) {
        if (success && cameraImageUri != null) {
            onImagePicked(cameraImageUri!!)
        }
    }

    @OptIn(ExperimentalPermissionsApi::class)
    fun launchCamera(launcher: ManagedActivityResultLauncher<Uri, Boolean>) {
        if (cameraPermissionState.status.isGranted) {
            cameraImageUri = createImageUri()
            launcher.launch(cameraImageUri!!)
        } else {
            cameraPermissionState.launchPermissionRequest()
        }
    }

    fun launchGallery(launcher: ManagedActivityResultLauncher<String, Uri?>) {
        launcher.launch("image/*")
    }
}