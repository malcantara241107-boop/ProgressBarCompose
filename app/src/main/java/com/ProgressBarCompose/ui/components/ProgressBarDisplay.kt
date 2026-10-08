package com.ProgressBarCompose.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ProgressBarCompose.R
import com.ProgressBarCompose.data.ProgressBarUiState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.runtime.getValue
import androidx.compose.animation.core.snap

@Composable
fun ProgressBarDisplay(
    uiState: ProgressBarUiState,
    modifier: Modifier = Modifier
) {
    val progress = if (uiState.maxValue == uiState.minValue) {
        0f
    } else {
        (
                (uiState.currentValue - uiState.minValue).toFloat() /
                        (uiState.maxValue - uiState.minValue).toFloat()
                ).coerceIn(0f, 1f)
    }

    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = if (uiState.animationsEnabled) {
            tween(durationMillis = uiState.animationDuration)
        } else {
            snap()
        },
        label = "progressAnimation"
    )

    // Barra Multicolor
    val progressColor = if (uiState.multicolorEnabled) {
        when {
            progress < 0.33f -> Color.Red
            progress < 0.66f -> Color.Yellow
            else -> Color(uiState.progressColor)
        }
    } else {
        Color(uiState.progressColor)
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {

                if (uiState.showImage) {
                    Image(
                        painter = painterResource(id = R.drawable.icon_default),
                        contentDescription = "Imagen de la barra",
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(8.dp)),
                        contentScale = ContentScale.Crop
                    )

                    Spacer(modifier = Modifier.size(12.dp))
                }

                Text(
                    text = uiState.title,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Caja
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(22.dp)
                    .border(
                        width = 2.dp,
                        color = Color(uiState.borderColor),
                        shape = RoundedCornerShape(4.dp)
                    )
                    .padding(3.dp)
                    .background(
                        color = Color(uiState.backgroundColor),
                        shape = RoundedCornerShape(2.dp)
                    )
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(animatedProgress)
                        .fillMaxHeight()
                        .background(
                            color = progressColor,
                            shape = RoundedCornerShape(2.dp)
                        )
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            if (uiState.showCurrentValue) {
                Text(
                    text = buildString {
                        append(uiState.currentValue)

                        if (uiState.showMaxValue) {
                            append(" / ")
                            append(uiState.maxValue)
                        }
                    },
                    modifier = Modifier.align(Alignment.End)
                )
            }
        }
    }
}