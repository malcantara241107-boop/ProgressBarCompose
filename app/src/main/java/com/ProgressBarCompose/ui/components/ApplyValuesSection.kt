package com.ProgressBarCompose.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun ApplyValuesSection(
    expanded: Boolean,
    valueToApply: String,
    onExpandChange: () -> Unit,
    onValueChange: (String) -> Unit,
    onApplyClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors()
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Aplicar Valores",
                    modifier = Modifier.weight(1f)
                )

                IconButton(
                    onClick = onExpandChange
                ) {
                    Icon(
                        imageVector = if (expanded) {
                            Icons.Default.ArrowDropUp
                        } else {
                            Icons.Default.ArrowDropDown
                        },
                        contentDescription = "Expandir o contraer"
                    )
                }
            }

            if (expanded) {
                OutlinedTextField(
                    value = valueToApply,
                    onValueChange = onValueChange,
                    modifier = Modifier.fillMaxWidth(),
                    label = {
                        Text("Valor a aplicar")
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    )
                )

                Button(
                    onClick = onApplyClick,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Aplicar")
                }
            }
        }
    }
}