package com.ProgressBarCompose.data

enum class IntervalValidationError {
    EMPTY,
    INVALID_FORMAT,
    TOO_SMALL,
    TOO_LARGE
}

data class IntervalConfig(
    val id: Int,
    val changeValue: String = "5",
    val intervalSeconds: String = "1",
    val enabled: Boolean = false,
    val validationError: IntervalValidationError? = null
)

data class ProgressBarSettings(
    val title: String = "Progreso",

    val showImage: Boolean = true,
    val showCurrentValue: Boolean = false,
    val showMaxValue: Boolean = false,

    val progressColor: Long = 0xFF4CAF50,
    val backgroundColor: Long = 0xFFE0E0E0,
    val borderColor: Long = 0xFF333333,

    val maxValue: String = "100",
    val minValue: String = "0",
    val currentValue: String = "100",

    val multicolorEnabled: Boolean = false,
    val animationsEnabled: Boolean = false
)

// Clase para la validación de errores en CONFIGURACIONES - LIMITES DE VALORES
data class SettingsValidationError(
    val minValueError: String? = null,
    val maxValueError: String? = null,
    val currentValueError: String? = null
)

data class ProgressBarUiState(
    val title: String = "Progreso",

    val currentValue: Long = 100L,
    val animationDuration: Int = 500,
    val minValue: Long = 0L,
    val maxValue: Long = 100L,

    val showImage: Boolean = true,
    val showCurrentValue: Boolean = false,
    val showMaxValue: Boolean = false,

    val progressColor: Long = 0xFF4CAF50,
    val backgroundColor: Long = 0xFFE0E0E0,
    val borderColor: Long = 0xFF333333,

    val valueToApply: String = "",
    val applyValuesExpanded: Boolean = true,

    val intervals: List<IntervalConfig> = listOf(
        IntervalConfig(
            id = 1,
            changeValue = "5",
            intervalSeconds = "1"
        ),
        IntervalConfig(
            id = 2,
            changeValue = "5",
            intervalSeconds = "2"
        ),
        IntervalConfig(
            id = 3,
            changeValue = "-5",
            intervalSeconds = "5"
        ),
        IntervalConfig(
            id = 4,
            changeValue = "10",
            intervalSeconds = "10"
        )
    ),

    val applyIntervalsExpanded: Boolean = true,

    val settingsDialogVisible: Boolean = false,

    val settingsDraft: ProgressBarSettings = ProgressBarSettings(),

    val settingsValidationError: SettingsValidationError? = null,

    val multicolorEnabled: Boolean = false,
    val animationsEnabled: Boolean = false
)