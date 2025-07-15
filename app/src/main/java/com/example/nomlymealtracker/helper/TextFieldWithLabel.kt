package com.example.nomlymealtracker.helper

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.nomlymealtracker.ui.theme.MidOrange
import com.example.nomlymealtracker.ui.theme.NomlyMealTrackerTheme

/**
 * Preview for TextFieldWithLabel()
 */
@Preview
@Composable
fun TextFieldWithLabelPreview(){
    NomlyMealTrackerTheme {
        TextFieldWithLabel(
            label = "Title",
            value = "Sample Title",
            onValueChange = {}
        )
    }
}

/**
 * A reusable labeled text field composable with support for password masking,
 * input type restriction, character limits, and optional character counter.
 * @param label The text label displayed above the text field.
 * @param value The current value of the input field.
 * @param onValueChange Callback triggered when the input value changes.
 * @param isPassword If true, input is masked (e.g., for passwords).
 * @param keyboardType The [KeyboardType] used for the input (e.g., text, number, email).
 * @param maxLength Optional maximum character limit for the input.
 * @param showCharCount If true, displays the current character count and limit.
 * @param numericOnly If true, restricts input to numeric values with optional decimals.
 */
@Composable
fun TextFieldWithLabel(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    isPassword: Boolean = false,
    keyboardType: KeyboardType = KeyboardType.Text,
    maxLength: Int? = null,
    showCharCount: Boolean = false,
    numericOnly: Boolean = false,
) {
    val visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None
    Text(text = label, style = MaterialTheme.typography.labelLarge)
    Spacer(modifier = Modifier.height(4.dp))
    TextField(
        value = value,
        onValueChange = {
            val withinLength = maxLength == null || it.length <= maxLength
            val isValidNumber = it.isEmpty() || it.matches(Regex("^\\d*\\.?\\d*\$"))

            if (withinLength && (!numericOnly || isValidNumber)) {
                onValueChange(it)
            }
        },
        placeholder = { Text(label) },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        visualTransformation = visualTransformation,
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = keyboardType
        ),
        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = MidOrange,
            focusedContainerColor = MidOrange
        )
    )

    if (showCharCount && maxLength != null) {
        Text(
            text = "${value.length}/$maxLength",
            style = MaterialTheme.typography.bodySmall,
            color = if (value.length > maxLength) Color.Red else Color.Gray,
        )
    }
}