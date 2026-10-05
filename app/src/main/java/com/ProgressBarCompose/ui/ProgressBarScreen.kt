package com.ProgressBarCompose.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ProgressBarCompose.data.ProgressBarUiState
import com.ProgressBarCompose.ui.components.BottomContent
import com.ProgressBarCompose.ui.components.ProgressBarDisplay
import com.ProgressBarCompose.ui.components.ProgressBarSettingsDialog   // Para la UI de CONFIGURACIÓN.

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProgressBarScreen(
    uiState: ProgressBarUiState,

    // Abrir y cerrar modales
    onSettingsClick: () -> Unit,
    onCloseSettings: () -> Unit,

    onValueChange: (String) -> Unit,
    onApplyValue: () -> Unit,
    onToggleApplyValues: () -> Unit,

    // Callbacks para INTERVALOS de BottonContent.kt
    onIntervalsExpandChange: () -> Unit,
    onIntervalChangeValue: (Int, String) -> Unit,
    onIntervalSecondsChange: (Int, String) -> Unit,
    onIntervalEnabledChange: (Int, Boolean) -> Unit,
    modifier: Modifier = Modifier,

    // Callbacks para CONFIGURACIONES
    onSettingsTitleChange: (String) -> Unit,
    onSettingsShowImageChange: (Boolean) -> Unit,
    onSettingsShowCurrentValueChange: (Boolean) -> Unit,
    onSettingsShowMaxValueChange: (Boolean) -> Unit,
    onSettingsMaxValueChange: (String) -> Unit,
    onSettingsMinValueChange: (String) -> Unit,
    onSettingsCurrentValueChange: (String) -> Unit,
    onSettingsProgressColorChange: (Long) -> Unit,
    onSettingsBackgroundColorChange: (Long) -> Unit,
    onSettingsBorderColorChange: (Long) -> Unit,
    onApplySettings: () -> Unit,
    onResetSettings: () -> Unit,
)
{
    Column(
        modifier = modifier.fillMaxSize()
    ) {

        TopAppBar(
            title = {
                Text(text = "Progress Bar")
            },
            actions = {
                IconButton(
                    onClick = onSettingsClick
                ) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Configuración"
                    )
                }
            }
        )

        ProgressBarDisplay(
            uiState = uiState,
            modifier = Modifier.padding(top = 8.dp)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {
            BottomContent(
                expanded = uiState.applyValuesExpanded,
                valueToApply = uiState.valueToApply,
                intervalsExpanded = uiState.applyIntervalsExpanded,
                intervals = uiState.intervals,
                onExpandChange = onToggleApplyValues,
                onValueChange = onValueChange,
                onApplyClick = onApplyValue,

                // Callbacks para INTERVALOS de BottonContent.kt
                onIntervalsExpandChange = onIntervalsExpandChange,
                onIntervalChangeValue = onIntervalChangeValue,
                onIntervalSecondsChange = onIntervalSecondsChange,
                onIntervalEnabledChange = onIntervalEnabledChange
            )
        }
        if (uiState.settingsDialogVisible) {
            ProgressBarSettingsDialog(
                settings = uiState.settingsDraft,
                onDismiss = onCloseSettings,
                onTitleChange = onSettingsTitleChange,
                onShowImageChange = onSettingsShowImageChange,
                onShowCurrentValueChange = onSettingsShowCurrentValueChange,
                onShowMaxValueChange = onSettingsShowMaxValueChange,
                onMaxValueChange = onSettingsMaxValueChange,
                onMinValueChange = onSettingsMinValueChange,
                onCurrentValueChange = onSettingsCurrentValueChange,
                maxValueError = uiState.settingsValidationError?.maxValueError,
                minValueError = uiState.settingsValidationError?.minValueError,
                currentValueError = uiState.settingsValidationError?.currentValueError,

                onProgressColorChange = onSettingsProgressColorChange,
                onBackgroundColorChange = onSettingsBackgroundColorChange,
                onBorderColorChange = onSettingsBorderColorChange,

                onApplyChanges = onApplySettings,
                onResetSettings = onResetSettings,
            )
        }
    }
}