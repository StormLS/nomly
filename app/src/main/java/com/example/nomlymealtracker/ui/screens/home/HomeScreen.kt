package com.example.nomlymealtracker.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
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
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.nomlymealtracker.Helper
import com.example.nomlymealtracker.data.models.Meal
import com.example.nomlymealtracker.data.models.MealType
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

// --- DUMMY DATA AND TYPES FOR A DASHBOARD BUILD
private val mealTypes = listOf("Breakfast", "Lunch", "Dinner")
private val macroTypes = listOf("Protein", "Carbs", "Fats")
// --- DUMMY DATA AND TYPES FOR A DASHBOARD BUILD

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun HomeScreenContentPreview(){
    val dummySelectedMealTypes = remember { mutableStateListOf<String>() }
    val dummySelectedMacros = remember { mutableStateListOf<String>() }
    val dummyShowBottomSheet = remember { mutableStateOf(false) }

    NomlyMealTrackerTheme() {
        HomeScreenContent(
            scrollState = rememberLazyListState(),
            coroutineScope = rememberCoroutineScope(),

            selectedMealTypes = dummySelectedMealTypes,
            selectedMacros = dummySelectedMacros,
            showBottomSheet = dummyShowBottomSheet,
            sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),

            searchText = "",
            onSearchTermChange = {},
            meals = emptyList(),
            isLoading = false
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    snackbarHost: SnackbarHostState,
    coroutineScope: CoroutineScope,
    viewModel: HomeViewModel = viewModel()
) {
    println("Opening HomeScreen")

    val scrollState = rememberLazyListState()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val showBottomSheet = remember { mutableStateOf(false) }

    val selectedMealTypes = remember { mutableStateListOf<String>() }
    val selectedMacros = remember { mutableStateListOf<String>() }

    val searchText = viewModel.searchTerm

    val meals by viewModel.meals.collectAsState()
    val isLoading = viewModel.mealsIsLoading

    HomeScreenContent(
        scrollState,
        sheetState,
        coroutineScope,

        selectedMealTypes,
        selectedMacros,

        showBottomSheet,

        searchText,
        onSearchTermChange = { viewModel.searchTerm = it },

        meals = meals,
        isLoading = isLoading,
    )
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun HomeScreenContent(
    scrollState: LazyListState,
    sheetState: SheetState,
    coroutineScope: CoroutineScope,

    selectedMealTypes: MutableList<String>,
    selectedMacros: MutableList<String>,

    showBottomSheet: MutableState<Boolean>,

    searchText: String,
    onSearchTermChange: (String) -> Unit,

    meals: List<Meal>,
    isLoading: Boolean,
){
    if (showBottomSheet.value) {
        ModalBottomSheet(
            onDismissRequest = { showBottomSheet.value },
            sheetState = sheetState,
            dragHandle = { BottomSheetDefaults.DragHandle() }
        ) {
            // Replace with real filters
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Filter Meals")
                // e.g. FilterChip(), Checkboxes, etc.
            }
        }
    }

    Scaffold(
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = { Text("Add Meal") },
                onClick = {},
                icon = { Icon(Icons.Default.Add, contentDescription = null) }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = searchText,
                    onValueChange = onSearchTermChange
                    ,
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp),
                    placeholder = { Text("Search meals...") },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                IconButton(
                    onClick = {
                        coroutineScope.launch {
                            showBottomSheet.value = true
                            sheetState.show()
                        }
                    }
                ) {
                    Icon(
                        Icons.Default.List,
                        contentDescription = "Filter"
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(horizontal = 4.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(mealTypes) { type ->
                    FilterChip(
                        selected = selectedMealTypes.contains(type),
                        onClick = {
                            if (selectedMealTypes.contains(type))
                                selectedMealTypes.remove(type)
                            else
                                selectedMealTypes.add(type)
                        },
                        label = { Text(type) },
                        modifier = Modifier.height(36.dp)
                    )
                }

                item {
                    Box(
                        modifier = Modifier
                            .height(36.dp)
                            .width(1.dp)
                            .background(MaterialTheme.colorScheme.outline)
                    )
                }

                items(macroTypes) { macro ->
                    FilterChip(
                        selected = selectedMacros.contains(macro),
                        onClick = {
                            if (selectedMacros.contains(macro))
                                selectedMacros.remove(macro)
                            else
                                selectedMacros.add(macro)
                        },
                        label = { Text(macro) },
                        modifier = Modifier.height(36.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 75.dp),
                state = scrollState,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (meals.isEmpty()) {
                    item {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            text = "You haven't added any meals yet",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                } else {
                    items(meals) { meal ->
                        MealCard(meal)
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun MealCardPreview(){
    NomlyMealTrackerTheme() {
        MealCard(
            Meal(
                title = "Grilled Chicken Salad",
                description = "Grilled chicken breast with mixed greens, cherry tomatoes, cucumbers, and olive oil dressing.",
                type = MealType.LUNCH,
                calories = 450,
                protein = 35,
                carbs = 10,
                fats = 20,
                portionSize = "1 plate",
                imageUrl = "https://example.com/images/chicken_salad.jpg",
                id = "meal2"
            )
        )
    }
}

@Composable
fun MealCard(meal: Meal){
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = meal.title,
                    modifier = Modifier.weight(1f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.titleMedium
                )

                Text(
                    textAlign = TextAlign.End,
                    text = Helper.formatTimestamp(meal.timestamp),
                    style = MaterialTheme.typography.bodyMedium, fontStyle = FontStyle.Italic
                )
            }
            Text(

                text = meal.description,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth().horizontalScroll(state = rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                    tonalElevation = 2.dp
                ) {
                    Text(
                        text = meal.type.displayName,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                meal.protein?.let{
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                        tonalElevation = 2.dp
                    ) {
                        Text(
                            text = "Protein ${meal.protein}",
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                meal.carbs?.let{
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                        tonalElevation = 2.dp
                    ) {
                        Text(
                            text = "Carbs ${meal.carbs}",
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                meal.fats?.let{
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                        tonalElevation = 2.dp
                    ) {
                        Text(
                            text = "Fats ${meal.fats}",
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }
}