package com.example.nomlymealtracker.ui.screens.home

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
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
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.nomlymealtracker.data.models.Meal
import com.example.nomlymealtracker.data.models.MealType
import com.example.nomlymealtracker.ui.screens.home.mealCard.MealCard
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope

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
    val dummyLogout = remember { mutableStateOf(false) }

    NomlyMealTrackerTheme() {
        HomeScreenContent(
            snackbarHost = SnackbarHostState(),
            scrollState = rememberLazyListState(),
            coroutineScope = rememberCoroutineScope(),

            selectedMealTypes = dummySelectedMealTypes,
            selectedMacros = dummySelectedMacros,
            showLogoutDialog = dummyLogout,
            showBottomSheet = dummyShowBottomSheet,
            sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),

            searchText = "",
            onSearchTermChange = {},
            meals = listOf(Meal(
                title = "Grilled Chicken Salad",
                description = "Grilled chicken breast with mixed greens, cherry tomatoes, cucumbers, and olive oil dressing.",
                type = MealType.LUNCH,
            )),
            isLoading = false,

            onLogoutClick = {},
            onViewMealClick = {},
            onAddNewMealClick = {}
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    snackbarHost: SnackbarHostState,
    coroutineScope: CoroutineScope,
    onLogoutClick: () -> Unit,
    onAddNewMealClick: () -> Unit,
    viewModel: HomeViewModel = viewModel()
) {
    println("Opening HomeScreen")

    val showLogoutDialog = remember { mutableStateOf(false) }
    val scrollState = rememberLazyListState()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val showBottomSheet = remember { mutableStateOf(false) }

    val selectedMealTypes = remember { mutableStateListOf<String>() }
    val selectedMacros = remember { mutableStateListOf<String>() }

    val searchText = viewModel.searchTerm

    val meals by viewModel.meals.collectAsState()
    val isLoading = viewModel.mealsIsLoading

    LaunchedEffect(navController.currentBackStackEntry) {
        viewModel.loadMeals()
    }

    HomeScreenContent(
        snackbarHost,
        scrollState,
        sheetState,
        coroutineScope,

        selectedMealTypes,
        selectedMacros,

        showLogoutDialog,
        showBottomSheet,

        searchText,
        onSearchTermChange = { viewModel.searchTerm = it },

        meals = meals,
        isLoading = isLoading,

        onLogoutClick = onLogoutClick,
        onViewMealClick = { meal ->
            navController.navigate("view_meal/${meal.mealId}")
        },
        onAddNewMealClick = { onAddNewMealClick() }
    )
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun HomeScreenContent(
    snackbarHost: SnackbarHostState,
    scrollState: LazyListState,
    sheetState: SheetState,
    coroutineScope: CoroutineScope,

    selectedMealTypes: MutableList<String>,
    selectedMacros: MutableList<String>,

    showLogoutDialog: MutableState<Boolean>,
    showBottomSheet: MutableState<Boolean>,

    searchText: String,
    onSearchTermChange: (String) -> Unit,

    meals: List<Meal>,
    isLoading: Boolean,

    onLogoutClick: () -> Unit,
    onViewMealClick: (Meal) -> Unit,
    onAddNewMealClick: () -> Unit,
){
    if (showBottomSheet.value) {
        ModalBottomSheet(
            onDismissRequest = { showBottomSheet.value = false },
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

    if (showLogoutDialog.value) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog.value = false },
            title = { Text("Confirm Logout") },
            text = { Text("Are you sure you want to log out and return to the login screen?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showLogoutDialog.value = false
                        FirebaseAuth.getInstance().signOut()
                        onLogoutClick()
                    }
                ) {
                    Text("Logout")
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog.value = false}) {
                    Text("Cancel")
                }
            }
        )
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHost) },
        topBar = {
            TopAppBar(
                title = { Text("Nomly Meal Tracker") },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            showLogoutDialog.value = true
                        }
                    ) {
                        Icon(
                            Icons.Default.ExitToApp,
                            contentDescription = "Logout",
                            modifier = Modifier.graphicsLayer {
                                scaleX = -1f
                            }
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = { Text("Add Meal") },
                onClick = onAddNewMealClick,
                containerColor = MaterialTheme.colorScheme.onPrimary,
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
                        modifier = Modifier.height(36.dp),
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primary,
                            selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
                        )
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
                        modifier = Modifier.height(36.dp),
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primary,
                            selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
                        )
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
                // If the Meals are still loading show my loader
                if (isLoading) {
                    item {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(
                                color = MaterialTheme.colorScheme.primary,
                                strokeWidth = 2.dp
                            )
                        }
                    }
                }

                // If its no longer laoding and their are no meals to show
                if (!isLoading && meals.isEmpty()) {
                    item {
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 24.dp),
                            textAlign = TextAlign.Center,
                            text = "You haven't added any meals yet",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }

                // Perfect case, show user meals
                if (!isLoading && meals.isNotEmpty()) {
                    items(meals) { meal ->
                        MealCard(
                            meal,
                            onClick = {
                                onViewMealClick(meal)
                            }
                        )
                    }
                }
            }
        }
    }
}


// Meal Card Preview composable for designing
@Preview
@Composable
fun MealCardPreview(){
    NomlyMealTrackerTheme() {
        MealCard(
            Meal(
                title = "Grilled Chicken Salad",
                description = "Grilled chicken breast with mixed greens, cherry tomatoes, cucumbers, and olive oil dressing.",
                type = MealType.LUNCH,
                calories = 450.0,
                protein = 35.0,
                carbs = 10.0,
                fats = 20.0,
                portionSize = "1 plate",
                imageBase64 = "someImageResource",
                id = "meal2"
            ),
            onClick = {}
        )
    }
}

