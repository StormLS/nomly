package com.example.nomlymealtracker.ui.screens.home.mealCard

import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.nomlymealtracker.helper.Helper
import com.example.nomlymealtracker.data.models.Meal
import com.example.nomlymealtracker.data.models.MealType
import com.example.nomlymealtracker.ui.theme.NomlyMealTrackerTheme

/**
 * Creating a Preview Friendly function for designing the Meal Card
 */
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

/**
 * Composable that displays a card representing a Meal.
 *
 * Shows the meal title, timestamp, description, and optional macronutrient details.
 * The entire card is clickable and triggers the provided [onClick] lambda.
 *
 * @param meal The [Meal] data object to display.
 * @param onClick Lambda to be invoked when the card is clicked.
 */
@Composable
fun MealCard(
    meal: Meal,
    onClick: () -> Unit
){
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                onClick = onClick
            ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.onPrimary
        ),
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
                            text = "Protein ${meal.protein}g",
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
                            text = "Carbs ${meal.carbs}g",
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
                            text = "Fats ${meal.fats}g",
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