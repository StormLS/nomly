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
