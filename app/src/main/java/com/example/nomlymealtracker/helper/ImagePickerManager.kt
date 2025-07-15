package com.example.nomlymealtracker.helper

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.compose.ManagedActivityResultLauncher
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted

/**
 * A helper class to manage image selection from camera or gallery,
 * including permission handling and MediaStore image creation.
 * @param context The application [Context] used for accessing content resolvers and MediaStore.
 * @param cameraPermissionState The permission state for accessing the device camera.
 * @param onImagePicked Callback triggered when an image is successfully picked or captured.
 */
class ImagePickerManager @OptIn(ExperimentalPermissionsApi::class) constructor(
    private val context: Context,
    private val cameraPermissionState: PermissionState,
    private val onImagePicked: (Uri) -> Unit
) {
    private var cameraImageUri: Uri? = null

    /**
     * Creates a [Uri] entry in [MediaStore] where the camera image will be saved.
     * @return A [Uri] pointing to the image storage location.
     * @throws IllegalStateException if the [MediaStore] insert fails.
     */
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

    /**
     * Handles the result from the image gallery picker.
     * @param uri The [Uri] returned from the gallery picker, or null if the selection was cancelled.
     */
    fun handleGalleryResult(uri: Uri?) {
        uri?.let { onImagePicked(it) }
    }

    /**
     * Handles the result of a camera capture operation.
     * @param success Indicates whether the image was successfully captured.
     */
    fun handleCameraResult(success: Boolean) {
        if (success && cameraImageUri != null) {
            onImagePicked(cameraImageUri!!)
        }
    }

    /**
     * Launches the camera intent to take a photo, after checking for camera permissions.
     * @param launcher A [ManagedActivityResultLauncher] used to start the camera activity.
     */
    @OptIn(ExperimentalPermissionsApi::class)
    fun launchCamera(launcher: ManagedActivityResultLauncher<Uri, Boolean>) {
        if (cameraPermissionState.status.isGranted) {
            cameraImageUri = createImageUri()
            launcher.launch(cameraImageUri!!)
        } else {
            cameraPermissionState.launchPermissionRequest()
        }
    }

    /**
     * Launches the image gallery to select an existing photo.
     * @param launcher A [ManagedActivityResultLauncher] used to start the gallery picker.
     */
    fun launchGallery(launcher: ManagedActivityResultLauncher<String, Uri?>) {
        launcher.launch("image/*")
    }
}