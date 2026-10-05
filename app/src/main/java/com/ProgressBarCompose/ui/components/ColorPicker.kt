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

@Composable
fun ColorPicker(
    selectedColor: Long,
    onColorSelected: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = listOf(
        0xFF4CAF50,
        0xFF2196F3,
        0xFFF44336,
        0xFFFFC107,
        0xFF9C27B0,
        0xFFFF9800,
        0xFFE0E0E0,
        0xFF333333
    )

    Row(
        modifier = modifier.horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        colors.forEach { colorValue ->

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