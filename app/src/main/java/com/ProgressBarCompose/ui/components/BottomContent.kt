package com.ProgressBarCompose.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun BottomContent(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Controles",
            fontWeight = FontWeight.Bold
        )

        HorizontalDivider()

        Text(
            text = "Aquí se agregarán los controles para aplicar valores e intervalos."
        )

        Text(
            text = "Esta sección tendrá desplazamiento vertical independiente."
        )

        Text(
            text = "Los controles se implementarán en los siguientes commits."
        )
    }
}