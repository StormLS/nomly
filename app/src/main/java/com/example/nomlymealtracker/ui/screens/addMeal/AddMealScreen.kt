package com.example.nomlymealtracker.ui.screens.addMeal

import android.Manifest
import android.content.ContentValues
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.nomlymealtracker.data.models.MealType
import com.example.nomlymealtracker.helper.ExposedDropdownMenu
import com.example.nomlymealtracker.helper.ImagePickerManager
import com.example.nomlymealtracker.helper.ImageSourceDialog
import com.example.nomlymealtracker.helper.TextFieldWithLabel
import com.example.nomlymealtracker.ui.theme.LightOrange
import com.example.nomlymealtracker.ui.theme.MidOrange
import com.example.nomlymealtracker.ui.theme.NomlyMealTrackerTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Preview
@Composable
fun AddMealScreenPreview() {
    NomlyMealTrackerTheme {
        AddMealScreenContent(
            snackbarHost = SnackbarHostState(),

            title = "",
            description = "",
            timeOfConsumption = "",
            selectedMealType = MealType.BREAKFAST,
            portionSize = "",
            protein = "",
            carbs = "",
            fats = "",
            calories = "",

            selectedImageUri = null,

            onTitleChange = {},
            onDescriptionChange = {},
            onTimeOfConsumptionChange = {},
            onSelectedMealTypeChange = {},
            onPortionSizeChange = {},
            onProteinChange = {},
            onCarbsChange = {},
            onFatsChange = {},
            onCaloriesChange = {},

            onAddImageClick = {},
            onSubmitClick = {},
            onBackClick = {},

            isSubmitting = false
        )
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun AddMealScreen(
    snackbarHost: SnackbarHostState,
    coroutineScope: CoroutineScope,
    onBackClick: () -> Unit,
    viewModel: AddMealViewModel = viewModel(),
) {
    // Main Screen input fields
    val title = viewModel.title
    val description = viewModel.description
    val timeOfConsumption = viewModel.timeOfConsumption
    val selectedMealType = viewModel.selectedMealType
    val portionSize = viewModel.portionSize
    val protein = viewModel.protein
    val carbs = viewModel.carbs
    val fats = viewModel.fats
    val calories = viewModel.calories

    val errorMessage = viewModel.errorMessage
    val successMessage = viewModel.successMessage

    val isSubmitting = viewModel.isSubmitting

    // --- Camera and Gallery options
    val context = LocalContext.current
    val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA)
    val imagePickerManager = remember { ImagePickerManager(context, cameraPermissionState) { viewModel.imageUri = it } }
    val selectedImageUri = viewModel.imageUri
    var showImageSourceDialog by remember { mutableStateOf(false) }
    val galleryLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { imagePickerManager.handleGalleryResult(it) }
    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { imagePickerManager.handleCameraResult(it) }
    if (showImageSourceDialog) {
        ImageSourceDialog(
            onDismiss = { showImageSourceDialog = false },
            onCameraClick = {
                showImageSourceDialog = false
                imagePickerManager.launchCamera(cameraLauncher)
            },
            onGalleryClick = {
                showImageSourceDialog = false
                imagePickerManager.launchGallery(galleryLauncher)
            }
        )
    }
    // --- Camera and Gallery options

    LaunchedEffect(successMessage, errorMessage) {
        errorMessage?.let {
            coroutineScope.launch {
                viewModel.errorMessage = null
                snackbarHost.showSnackbar(it, duration = SnackbarDuration.Short)
            }
        }
        successMessage?.let {
            onBackClick()
            snackbarHost.showSnackbar(it, duration = SnackbarDuration.Long)
        }
    }

    AddMealScreenContent(
        snackbarHost = snackbarHost,

        title = title,
        description = description,
        timeOfConsumption = timeOfConsumption,
        selectedMealType = selectedMealType,
        portionSize = portionSize,
        protein = protein,
        carbs = carbs,
        fats = fats,
        calories = calories,

        selectedImageUri = selectedImageUri,

        onTitleChange = { viewModel.title = it },
        onDescriptionChange = { viewModel.description = it },
        onTimeOfConsumptionChange = { viewModel.timeOfConsumption = it },
        onSelectedMealTypeChange = { viewModel.selectedMealType = it },
        onPortionSizeChange = { viewModel.portionSize = it },
        onProteinChange = { viewModel.protein = it },
        onCarbsChange = { viewModel.carbs = it },
        onFatsChange = { viewModel.fats = it },
        onCaloriesChange = { viewModel.calories = it },

        onAddImageClick = { showImageSourceDialog = true },
        onSubmitClick = { viewModel.submitMeal(context) },
        onBackClick = { onBackClick() },

        isSubmitting = isSubmitting
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddMealScreenContent(
    snackbarHost: SnackbarHostState,

    title: String,
    description: String,
    timeOfConsumption: String,
    selectedMealType: MealType,
    portionSize: String,
    protein: String,
    carbs: String,
    fats: String,
    calories: String,

    selectedImageUri: Uri?,

    onTitleChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onTimeOfConsumptionChange: (String) -> Unit,
    onSelectedMealTypeChange: (MealType) -> Unit,
    onPortionSizeChange: (String) -> Unit,
    onProteinChange: (String) -> Unit,
    onCarbsChange: (String) -> Unit,
    onFatsChange: (String) -> Unit,
    onCaloriesChange: (String) -> Unit,

    onAddImageClick: () -> Unit,
    onSubmitClick: () -> Unit,
    onBackClick: () -> Unit,

    isSubmitting: Boolean
){
    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHost) },
        topBar = {
            TopAppBar(
                title = { Text("Add Meal") },
                navigationIcon = {
                    IconButton(
                        onClick = onBackClick
                    ) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(LightOrange)
                .padding(innerPadding)
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(MidOrange)
                    .clickable { onAddImageClick() },
                contentAlignment = Alignment.Center
            ) {
                if (selectedImageUri != null) {
                    Image(
                        painter = rememberAsyncImagePainter(selectedImageUri),
                        contentDescription = "Selected Image",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.Image,
                            contentDescription = "Add Image",
                            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                        Text(
                            text = "Add Image (Optional)",
                            style = MaterialTheme.typography.labelLarge,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            TextFieldWithLabel("Title (100 Characters) *", title, onTitleChange, isPassword = false, maxLength = 100, showCharCount = true)
            Spacer(modifier = Modifier.height(24.dp))
            TextFieldWithLabel("Description (300 Characters) *", description, onDescriptionChange, isPassword = false, maxLength = 300, showCharCount = true)
            Spacer(modifier = Modifier.height(24.dp))
            TextFieldWithLabel("Time at Consumption *", timeOfConsumption, onTimeOfConsumptionChange, isPassword = false)

            Spacer(modifier = Modifier.height(24.dp))

            Text(text = "Meal Type *", style = MaterialTheme.typography.labelLarge)
            ExposedDropdownMenu(selectedMealType, onSelectedMealTypeChange)

            Spacer(modifier = Modifier.height(24.dp))

            TextFieldWithLabel("Quantity / Portion Size *", portionSize, onPortionSizeChange, isPassword = false)

            Spacer(modifier = Modifier.height(24.dp))

            Text(text = "Macronutrients (Optional)", style = MaterialTheme.typography.labelLarge)
            TextFieldWithLabel("Protein (g)", protein, onProteinChange, isPassword = false, keyboardType = KeyboardType.Number, numericOnly = true)
            Spacer(modifier = Modifier.height(24.dp))
            TextFieldWithLabel("Carbs (g)", carbs, onCarbsChange, isPassword = false, keyboardType = KeyboardType.Number, numericOnly = true)
            Spacer(modifier = Modifier.height(24.dp))
            TextFieldWithLabel("Fats (g)", fats, onFatsChange, isPassword = false, keyboardType = KeyboardType.Number, numericOnly = true)
            Spacer(modifier = Modifier.height(24.dp))
            TextFieldWithLabel("Calories", calories, onCaloriesChange, isPassword = false, keyboardType = KeyboardType.Number, numericOnly = true)

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    println("Just clicked button to submit meal")
                    onSubmitClick()
                },
                enabled = !isSubmitting,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                if (isSubmitting) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("Add")
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedButton(
                onClick = onBackClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Back")
            }
        }
    }
}

