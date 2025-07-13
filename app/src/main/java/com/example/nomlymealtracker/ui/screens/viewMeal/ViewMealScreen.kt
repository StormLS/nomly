package com.example.nomlymealtracker.ui.screens.viewMeal

import android.view.View
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.unit.dp
import com.example.nomlymealtracker.ui.theme.NomlyMealTrackerTheme
import kotlinx.coroutines.launch
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetState
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.nomlymealtracker.data.models.Meal
import com.example.nomlymealtracker.data.models.MealType
import com.example.nomlymealtracker.ui.screens.home.HomeViewModel
import com.example.nomlymealtracker.ui.screens.home.mealCard.MealCard
import kotlinx.coroutines.CoroutineScope

// Preview for the ViewMealScreen
@Preview
@Composable
fun ViewMealScreenContentPreview(){
    NomlyMealTrackerTheme {
        ViewMealScreenContent(
            snackbarHost = SnackbarHostState(),

            title = "Double Cheese Burger",
            description = "A really long explanation of this burger and what its made of. Surprice, its mostly beef and some onions. Yum",
            timeOfConsumption = "14:22pm",
            selectedMealType = MealType.BREAKFAST,
            portionSize = "1x",
            protein = "60",
            carbs = "39",
            fats = "12",
            calories = "387",

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
    println("Meal Id = $mealId")

    val meal by viewModel.getMeal(mealId).collectAsState(initial = null)

    Box(modifier = Modifier.fillMaxSize()) {
        if (meal == null) {
            // Centered loader
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        } else {
            // Your content
            ViewMealScreenContent(
                snackbarHost = snackbarHost,

                title = meal!!.title,
                description = meal!!.description,
                timeOfConsumption = meal!!.timestamp.toDate().toString(),
                selectedMealType = meal!!.type,
                portionSize = meal!!.portionSize,
                protein = meal!!.protein?.toString(),
                carbs = meal!!.carbs?.toString(),
                fats = meal!!.fats?.toString(),
                calories = meal!!.calories?.toString(),

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

    title: String,
    description: String,
    timeOfConsumption: String,
    selectedMealType: MealType,
    portionSize: String,
    protein: String?,
    carbs: String?,
    fats: String?,
    calories: String?,

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
                    IconButton(onClick = {
                        // TODO: Handle share action
                    }) {
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
                    .background(MaterialTheme.colorScheme.primary),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Image,
                    contentDescription = "Meal Image",
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
                Text(
                    text = "Image Reference",
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier.padding(top = 48.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(title, style = MaterialTheme.typography.titleLarge)
            Text(description, style = MaterialTheme.typography.bodyLarge)

            Spacer(modifier = Modifier.height(12.dp))

            Text("Time Eaten", style = MaterialTheme.typography.titleLarge)
            Text(timeOfConsumption, style = MaterialTheme.typography.bodyLarge)

            Spacer(modifier = Modifier.height(12.dp))

            Text("Meal Type", style = MaterialTheme.typography.titleLarge)
            Text(selectedMealType.name, style = MaterialTheme.typography.bodyLarge)

            Spacer(modifier = Modifier.height(12.dp))

            Text("Portion Size", style = MaterialTheme.typography.titleLarge)
            Text(portionSize, style = MaterialTheme.typography.bodyLarge)

            Spacer(modifier = Modifier.height(12.dp))

            Text("Macronutrients", style = MaterialTheme.typography.titleLarge)
            MacronutrientRow(label = "Protein", value = protein)
            MacronutrientRow(label = "Carbs", value = carbs)
            MacronutrientRow(label = "Fats", value = fats)
            Spacer(modifier = Modifier.height(6.dp))
            MacronutrientRow(label = "Calories", value = calories)
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
            text = value ?: "-",
            style = MaterialTheme.typography.bodyLarge
        )
    }
}