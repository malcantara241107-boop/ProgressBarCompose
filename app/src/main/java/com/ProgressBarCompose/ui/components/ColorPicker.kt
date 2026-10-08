package com.ProgressBarCompose.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
// Scrollbar
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState

import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Surface
import androidx.compose.foundation.layout.heightIn
// UI Overhaul
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme


@Composable
fun ColorPicker(
    selectedColor: Long,
    onColorSelected: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = listOf(
        0xFF4CAF50, // Verde
        0xFF2196F3, // Azul
        0xFFF44336, // Rojo
        0xFFFFC107, // Amarillo
        0xFF9C27B0, // Morado
        0xFFFF9800, // Naranja
        0xFF4a90e2, // Cielo
        0xFF6d7b98, // Gris turquesa
        0xFF9aa7c1, // Gris claro turquesa
        0xFFf2f5ff, // Blanco Celeste
        0xFF17233c, // Azul oscuro
        0xFFE0E0E0, // Blanco Default
        0xFF333333 // Negro Default
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        val firstRow = colors.filterIndexed { index, _ -> index % 2 == 0 }
        val secondRow = colors.filterIndexed { index, _ -> index % 2 != 0 }

        val scrollState = rememberScrollState()
        val firstScrollState = rememberScrollState()
        val secondScrollState = rememberScrollState()

        /*
        Column(
            modifier = modifier
                .fillMaxWidth()
                .background(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(12.dp)
                )
                .horizontalScroll(scrollState)
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
         */
        /*
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 650.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
         */

        Column(
            modifier = modifier
                .fillMaxWidth()
                .background(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(12.dp)
                )
                .horizontalScroll(scrollState)
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                firstRow.forEach { colorValue ->

                    val isSelected = colorValue == selectedColor

                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(
                                color = Color(colorValue),
                                shape = CircleShape
                            )
                            .then(
                                if (isSelected) {
                                    Modifier.border(
                                        width = 3.dp,
                                        color = Color.Black,
                                        shape = CircleShape
                                    )
                                } else {
                                    Modifier
                                }
                            )
                            .clickable {
                                onColorSelected(colorValue)
                            }
                    )
                }
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                secondRow.forEach { colorValue ->

                    val isSelected = colorValue == selectedColor

                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(
                                color = Color(colorValue),
                                shape = CircleShape
                            )
                            .then(
                                if (isSelected) {
                                    Modifier.border(
                                        width = 3.dp,
                                        color = Color.Black,
                                        shape = CircleShape
                                    )
                                } else {
                                    Modifier
                                }
                            )
                            .clickable {
                                onColorSelected(colorValue)
                            }
                    )
                }
            }
        }
    }
}