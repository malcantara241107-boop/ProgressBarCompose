package com.ProgressBarCompose.viewmodel

import androidx.lifecycle.ViewModel
import com.ProgressBarCompose.data.ProgressBarUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ProgressBarViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(ProgressBarUiState())

    val uiState: StateFlow<ProgressBarUiState> = _uiState.asStateFlow()

    fun updateValueToApply(value: String) {
        val filteredValue = value.filter { character ->
            character.isDigit() || character == '-'
        }

        _uiState.value = _uiState.value.copy(
            valueToApply = filteredValue
        )
    }

    fun applyValue() {
        val valueToApply = _uiState.value.valueToApply.toLongOrNull()
            ?: return

        val currentState = _uiState.value

        val newCurrentValue = (
                currentState.currentValue + valueToApply
                ).coerceIn(
                currentState.minValue,
                currentState.maxValue
            )

        _uiState.value = currentState.copy(
            currentValue = newCurrentValue,
            /* Establece un nuevo valor al aplicar uno.
            valueToApply = ""
             */
        )
    }

    fun toggleApplyValuesExpanded() {
        _uiState.value = _uiState.value.copy(
            applyValuesExpanded = !_uiState.value.applyValuesExpanded
        )
    }
}