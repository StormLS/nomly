package com.example.nomlymealtracker.ui.screens.viewMeal

import android.graphics.Bitmap
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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
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
import android.graphics.BitmapFactory
import android.util.Base64
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.material3.Card
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import coil.compose.rememberAsyncImagePainter
import com.example.nomlymealtracker.helper.Helper.decodeBase64ToImageBitmap

// Preview for the ViewMealScreen
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

            onShareClick = {},
            onBackClick = {}
        )
    }
}

// The Main entry composable for the ViewMealScreen
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

                onShareClick = { meal?.let { shareMeal(context, it) } },
                onBackClick = onBackClick
            )
        }
    }
}

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
                .padding(24.dp)
                .verticalScroll(rememberScrollState())
        ) {
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
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Default.Image,
                                contentDescription = "Add Image",
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
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                title,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary
            )
            Text(description, style = MaterialTheme.typography.bodyLarge)

            Spacer(modifier = Modifier.height(12.dp))

            Text("Time Eaten", style = MaterialTheme.typography.titleMedium)
            Text(timeOfConsumption , style = MaterialTheme.typography.bodyLarge)

            Spacer(modifier = Modifier.height(12.dp))

            Text("Meal Type", style = MaterialTheme.typography.titleMedium)
            Text(selectedMealType.name, style = MaterialTheme.typography.bodyLarge)

            Spacer(modifier = Modifier.height(12.dp))

            Text("Portion Size", style = MaterialTheme.typography.titleMedium)
            Text(portionSize, style = MaterialTheme.typography.bodyLarge)

            Spacer(modifier = Modifier.height(12.dp))

            Text("Macronutrients", style = MaterialTheme.typography.titleLarge)
            MacronutrientRow(label = "Protein", value = protein)
            MacronutrientRow(label = "Carbs", value = carbs)
            MacronutrientRow(label = "Fats", value = fats)
            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
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

            Spacer(modifier = Modifier.height(6.dp))

            Text("Created", style = MaterialTheme.typography.titleMedium)
            Text(Helper.formatTimestamp(timestamp), style = MaterialTheme.typography.bodyLarge)
        }
    }
}

@Composable
fun MacronutrientRow(label: String, value: String?) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            text = if (value.isNullOrBlank()) "N/A" else "${value}g",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.primary
        )
    }
}