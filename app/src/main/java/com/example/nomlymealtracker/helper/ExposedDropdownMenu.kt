package com.example.nomlymealtracker.helper

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.example.nomlymealtracker.data.models.MealType
import com.example.nomlymealtracker.ui.theme.MidOrange

/**
 * A composable dropdown menu for selecting a [MealType], using Material 3's ExposedDropdownMenuBox.
 * @param selectedMealType The currently selected [MealType] that will be shown in the TextField.
 * @param onMealTypeSelected Callback triggered when a [MealType] is selected from the dropdown.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExposedDropdownMenu(
    selectedMealType: MealType,
    onMealTypeSelected: (MealType) -> Unit
) {
    val expanded = remember { mutableStateOf(false) }
    val mealTypes = MealType.values()

    ExposedDropdownMenuBox(
        expanded = expanded.value,
        onExpandedChange = { expanded.value = !expanded.value }
    ) {
        TextField(
            readOnly = true,
            value = selectedMealType.name.lowercase().replaceFirstChar { it.uppercaseChar() },
            onValueChange = {},
            label = { Text("Select Meal Type") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded.value) },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = MidOrange,
                focusedContainerColor = MidOrange
            )
        )
        ExposedDropdownMenu(
            expanded = expanded.value,
            onDismissRequest = { expanded.value = false }
        ) {
            mealTypes.forEach { mealType ->
                DropdownMenuItem(
                    text = { Text(mealType.name) },
                    onClick = {
                        onMealTypeSelected(mealType)
                        expanded.value = false
                    }
                )
            }
        }
    }
}