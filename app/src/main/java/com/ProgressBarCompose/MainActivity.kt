package com.ProgressBarCompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ProgressBarCompose.ui.ProgressBarScreen
import com.ProgressBarCompose.ui.theme.MyApplicationTheme
import com.ProgressBarCompose.viewmodel.ProgressBarViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: ProgressBarViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            MyApplicationTheme {

                val uiState by viewModel.uiState.collectAsStateWithLifecycle()

                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->

                    ProgressBarScreen(
                        uiState = uiState,
                        onSettingsClick = viewModel::openSettings,
                        onCloseSettings = viewModel::closeSettings,
                        onValueChange = viewModel::updateValueToApply,
                        onApplyValue = viewModel::applyValue,
                        onToggleApplyValues = viewModel::toggleApplyValuesExpanded,
                        // Calbacks para INTERVALOS
                        onIntervalsExpandChange = viewModel::toggleApplyIntervalsExpanded,
                        onIntervalChangeValue = viewModel::updateIntervalChangeValue,
                        onIntervalSecondsChange = viewModel::updateIntervalSeconds,
                        onIntervalEnabledChange = viewModel::setIntervalEnabled,
                        // Callbacks para CONFIGURACIONES
                        onSettingsTitleChange = viewModel::updateSettingsTitle,
                        onSettingsShowImageChange = viewModel::updateSettingsShowImage,
                        onSettingsShowCurrentValueChange = viewModel::updateSettingsShowCurrentValue,
                        onSettingsShowMaxValueChange = viewModel::updateSettingsShowMaxValue,
                        modifier = Modifier.padding(innerPadding),
                        onSettingsMaxValueChange = viewModel::updateSettingsMaxValue,
                        onSettingsMinValueChange = viewModel::updateSettingsMinValue,
                        onSettingsCurrentValueChange = viewModel::updateSettingsCurrentValue,
                        onSettingsProgressColorChange = viewModel::updateSettingsProgressColor,
                        onSettingsBackgroundColorChange = viewModel::updateSettingsBackgroundColor,
                        onSettingsBorderColorChange = viewModel::updateSettingsBorderColor,
                        onApplySettings = viewModel::applySettings,
                        onResetSettings = viewModel::resetSettingsDraft
                    )
                }
            }
        }
    }
}