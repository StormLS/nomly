package com.example.nomlymealtracker.ui.screens.viewMeal

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.unit.dp
import com.example.nomlymealtracker.ui.theme.NomlyMealTrackerTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.example.nomlymealtracker.data.models.MealType
import com.example.nomlymealtracker.helper.Helper
import com.example.nomlymealtracker.helper.Helper.shareMeal
import com.example.nomlymealtracker.ui.theme.MidOrange
import com.google.firebase.Timestamp
import kotlinx.coroutines.CoroutineScope
import androidx.compose.foundation.Image
import androidx.compose.material3.Button
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.sp
import com.example.nomlymealtracker.helper.Helper.decodeBase64ToImageBitmap
import com.example.nomlymealtracker.helper.MacronutrientRow

/**
 * Creating a Preview Friendly function for designing the View Meal Screen
 */
@Preview
@Composable
fun ViewMealScreenContentPreview(){
    NomlyMealTrackerTheme {
        ViewMealScreenContent(
            snackbarHost = SnackbarHostState(),

            image = null,
            title = "Double Cheese Burger",
            description = "A really long explanation of this burger and what its made of. Surprice, its mostly beef and some onions. Yum",
            timeOfConsumption = "12:32pm",
            selectedMealType = MealType.BREAKFAST,
            portionSize = "1x",
            protein = "60",
            carbs = "39",
            fats = "12",
            calories = "387",
            timestamp = Timestamp.now(),
            onDeleteClick = {},
            onShareClick = {},
            onBackClick = {}
        )
    }
}

/**
 * Composable that displays the detailed view of a single meal identified by [mealId].
 * Loads the meal data from the provided [ViewMealViewModel], shows a loading indicator
 * while fetching, and displays the meal details including image, title, description,
 * time of consumption, macronutrients, and timestamp.
 *
 * Provides actions to share the meal and navigate back.
 *
 * @param mealId The unique identifier of the meal to display.
 * @param snackbarHost The [SnackbarHostState] for showing snackbars.
 * @param coroutineScope The [CoroutineScope] used for launching coroutines.
 * @param onBackClick Lambda invoked when the back navigation action is triggered.
 * @param viewModel The [ViewMealViewModel] providing the meal data (defaulted to viewModel()).
 */
@Composable
fun ViewMealScreen(
    mealId: String,
    snackbarHost: SnackbarHostState,
    coroutineScope: CoroutineScope,

    onBackClick: () -> Unit,

    viewModel: ViewMealViewModel = viewModel()
){
    // Screen opens and the meal item in context loads
    val meal by viewModel.meal.collectAsState()
    LaunchedEffect(mealId) {
        viewModel.getMeal(mealId)
    }

    val context = LocalContext.current

    val imageBitmap = remember(meal?.imageBase64) {
        meal?.imageBase64?.let { decodeBase64ToImageBitmap(it) }
    }

    // A box used to have a loader visible while the item loads
    Box(modifier = Modifier.fillMaxSize()) {
        if (meal == null) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        } else {
            ViewMealScreenContent(
                snackbarHost = snackbarHost,

                image = imageBitmap,
                title = meal!!.title,
                description = meal!!.description,
                timeOfConsumption = meal!!.timeEaten,
                selectedMealType = meal!!.type,
                portionSize = meal!!.portionSize,
                protein = meal!!.protein?.toString(),
                carbs = meal!!.carbs?.toString(),
                fats = meal!!.fats?.toString(),
                calories = meal!!.calories?.toString(),
                timestamp = meal!!.timestamp,

                onDeleteClick = { meal?.let { viewModel.deleteMeal(it.mealId) }},
                onShareClick = { meal?.let { shareMeal(context, it) } },
                onBackClick = onBackClick
            )
        }
    }
}


/**
 * Composable displaying detailed information about a meal.
 *
 * Shows the meal image (or placeholder if no image), title, description,
 * time eaten, meal type, portion size, macronutrients, calories, and
 * the creation timestamp.
 *
 * Includes a top app bar with navigation back and share action buttons.
 */
// The ViewMealScreen content
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewMealScreenContent(
    snackbarHost: SnackbarHostState,

    image: ImageBitmap?,
    title: String,
    description: String,
    timeOfConsumption: String,
    selectedMealType: MealType,
    portionSize: String,
    protein: String?,
    carbs: String?,
    fats: String?,
    calories: String?,
    timestamp: Timestamp,

    onDeleteClick: () -> Unit,
    onShareClick: () -> Unit,
    onBackClick: () -> Unit,
){
    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHost) },
        topBar = {
            TopAppBar(
                title = { Text("Meal Details") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = onShareClick) {
                        Icon(Icons.Default.Share, contentDescription = "Share")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.onPrimary)
                .padding(innerPadding)
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Meal Image
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(MidOrange),
                contentAlignment = Alignment.Center
            ) {
                if (image != null) {
                    Image(
                        bitmap = image,
                        contentDescription = "Meal Image",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.Image,
                            contentDescription = "No Image",
                            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                        Text(
                            text = "No Image Available",
                            style = MaterialTheme.typography.labelLarge,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Title & Description
            Text(
                title,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(description, style = MaterialTheme.typography.bodyLarge)

            Spacer(modifier = Modifier.height(16.dp))

            // Meal Info Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondary
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Meal Type chip
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                        tonalElevation = 2.dp
                    ) {
                        Text(
                            text = selectedMealType.displayName,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    // Time Eaten & Portion Size side by side
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Time Eaten", style = MaterialTheme.typography.labelMedium)
                            Text(timeOfConsumption, style = MaterialTheme.typography.bodyLarge)
                        }
                        Column(
                            modifier = Modifier.weight(1f),
                            horizontalAlignment = Alignment.End
                        ) {
                            Text("Portion Size", style = MaterialTheme.typography.labelMedium)
                            Text(portionSize, style = MaterialTheme.typography.bodyLarge)
                        }
                    }

                    // Created timestamp
                    Column {
                        Text("Created", style = MaterialTheme.typography.labelMedium)
                        Text(
                            Helper.formatTimestamp(timestamp),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Nutrition Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondary
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("Nutrition", style = MaterialTheme.typography.titleLarge)

                    MacronutrientRow(label = "Protein", value = protein)
                    MacronutrientRow(label = "Carbs", value = carbs)
                    MacronutrientRow(label = "Fats", value = fats)

                    Divider()

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Calories",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = calories ?: "N/A",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Delete Button
            Button(
                onClick = onDeleteClick,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(30),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                )
            ) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = null,
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text("Delete Entry", fontSize = 20.sp)
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}