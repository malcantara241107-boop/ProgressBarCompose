package com.ProgressBarCompose.data

data class ProgressBarUiState(
    val title: String = "Progreso",

    val currentValue: Long = 100L,
    val minValue: Long = 0L,
    val maxValue: Long = 100L,

    val showImage: Boolean = true,
    val showCurrentValue: Boolean = false,
    val showMaxValue: Boolean = false,

    val progressColor: Long = 0xFF4CAF50,
    val backgroundColor: Long = 0xFFE0E0E0,
    val borderColor: Long = 0xFF333333
)