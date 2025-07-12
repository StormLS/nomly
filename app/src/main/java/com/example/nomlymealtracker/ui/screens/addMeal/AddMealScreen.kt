package com.example.nomlymealtracker.ui.screens.addMeal

import androidx.compose.foundation.background
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
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.nomlymealtracker.data.models.MealType
import com.example.nomlymealtracker.helper.ExposedDropdownMenu
import com.example.nomlymealtracker.helper.TextFieldWithLabel
import com.example.nomlymealtracker.ui.theme.LightOrange
import com.example.nomlymealtracker.ui.theme.MidOrange
import com.example.nomlymealtracker.ui.theme.NomlyMealTrackerTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Preview
@Composable
fun AddMealScreenPreview() {
    NomlyMealTrackerTheme {
        AddMealScreen(
            navController = NavController(LocalContext.current),
            snackbarHost = SnackbarHostState(),
            coroutineScope = rememberCoroutineScope()
        )
    }
}

@Composable
fun AddMealScreen(
    navController: NavController,
    snackbarHost: SnackbarHostState,
    coroutineScope: CoroutineScope,
    viewModel: AddMealViewModel = viewModel(),
) {
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

    LaunchedEffect(successMessage, errorMessage) {
        errorMessage?.let {
            coroutineScope.launch {
                snackbarHost.showSnackbar(it, duration = SnackbarDuration.Short)
            }
        }
        successMessage?.let {
            coroutineScope.launch {
                //viewModel.clearRegisterSuccess()
                snackbarHost.showSnackbar(it, duration = SnackbarDuration.Long)
            }
        }
    }

    AddMealScreenContent(
        title = title,
        description = description,
        timeOfConsumption = timeOfConsumption,
        selectedMealType = selectedMealType,
        portionSize = portionSize,
        protein = protein,
        carbs = carbs,
        fats = fats,
        calories = calories,

        navController = navController,
        snackbarHost = snackbarHost,
        viewModel = viewModel,
        coroutineScope = coroutineScope,

        onTitleChange = { viewModel.title = it },
        onDescriptionChange = { viewModel.description = it },
        onTimeOfConsumptionChange = { viewModel.timeOfConsumption = it },
        onSelectedMealTypeChange = { viewModel.selectedMealType = it },
        onPortionSizeChange = { viewModel.portionSize = it },
        onProteinChange = { viewModel.protein = it },
        onCarbsChange = { viewModel.carbs = it },
        onFatsChange = { viewModel.fats = it },
        onCaloriesChange = { viewModel.calories = it },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddMealScreenContent(
    title: String,
    description: String,
    timeOfConsumption: String,
    selectedMealType: MealType,
    portionSize: String,
    protein: String,
    carbs: String,
    fats: String,
    calories: String,

    navController: NavController,
    snackbarHost: SnackbarHostState,
    viewModel: AddMealViewModel = viewModel(),
    coroutineScope: CoroutineScope,

    onTitleChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onTimeOfConsumptionChange: (String) -> Unit,
    onSelectedMealTypeChange: (MealType) -> Unit,
    onPortionSizeChange: (String) -> Unit,
    onProteinChange: (String) -> Unit,
    onCarbsChange: (String) -> Unit,
    onFatsChange: (String) -> Unit,
    onCaloriesChange: (String) -> Unit,
){
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add Meal") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
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
                    .background(MidOrange),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Image,
                    contentDescription = "Add Image",
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
                Text(
                    text = "Add Image Reference",
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier.padding(top = 48.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            TextFieldWithLabel("Title (100 Characters)", title, onTitleChange, isPassword = false)
            Spacer(modifier = Modifier.height(24.dp))
            TextFieldWithLabel("Description (300 Characters)", description, onDescriptionChange, isPassword = false)
            Spacer(modifier = Modifier.height(24.dp))
            TextFieldWithLabel("Time at Consumption", timeOfConsumption, onTimeOfConsumptionChange, isPassword = false)

            Spacer(modifier = Modifier.height(24.dp))

            Text(text = "Meal Type", style = MaterialTheme.typography.labelLarge)
            ExposedDropdownMenu(viewModel.selectedMealType, onSelectedMealTypeChange)

            Spacer(modifier = Modifier.height(24.dp))

            TextFieldWithLabel("Quantity / Portion Size", portionSize, onPortionSizeChange, isPassword = false)

            Spacer(modifier = Modifier.height(24.dp))

            Text(text = "Macronutrients (Optional)", style = MaterialTheme.typography.labelLarge)
            TextFieldWithLabel("Protein (g)", protein, onProteinChange, isPassword = false)
            Spacer(modifier = Modifier.height(24.dp))
            TextFieldWithLabel("Carbs (g)", carbs, onCarbsChange, isPassword = false)
            Spacer(modifier = Modifier.height(24.dp))
            TextFieldWithLabel("Fats (g)", fats, onFatsChange, isPassword = false)
            Spacer(modifier = Modifier.height(24.dp))
            TextFieldWithLabel("Calories", calories, onCaloriesChange, isPassword = false)

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    coroutineScope.launch {
                        viewModel.submitMeal(
                            onSuccess = {
                                coroutineScope.launch {
                                    //viewModel.clearRegisterError()
                                    snackbarHost.showSnackbar("Add a new Item Successfully")
                                    navController.popBackStack()
                                }
                            },
                            onError = {
                                coroutineScope.launch {
                                    snackbarHost.showSnackbar("Error: $it")
                                }

                            }
                        )
                    }
                },
                enabled = !viewModel.isSubmitting,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                if (viewModel.isSubmitting) {
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
                onClick = { navController.popBackStack() },
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

