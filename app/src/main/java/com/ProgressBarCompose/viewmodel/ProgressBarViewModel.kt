package com.ProgressBarCompose.viewmodel

import androidx.lifecycle.ViewModel
import com.ProgressBarCompose.data.ProgressBarUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

// Imports para el menu de intervalos
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// Importación para la validación de Intervalos
import com.ProgressBarCompose.data.IntervalValidationError

class ProgressBarViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(ProgressBarUiState())

    val uiState: StateFlow<ProgressBarUiState> = _uiState.asStateFlow()

    // Rangos aceptados para la cadencia en los valores aplicados automaticamente.
    private companion object {
        const val MIN_INTERVAL_SECONDS = 0.01
        const val MAX_INTERVAL_SECONDS = 256.0
    }

    // Variable para Intervalos
    private val intervalJobs = mutableMapOf<Int, Job>()

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

        applyChange(valueToApply)
    }

    fun toggleApplyValuesExpanded() {
        _uiState.value = _uiState.value.copy(
            applyValuesExpanded = !_uiState.value.applyValuesExpanded
        )
    }

    // Controla los intervalos y evita duplicar logica.
    private fun applyChange(changeValue: Long) {
        val currentState = _uiState.value

        val newCurrentValue = (
                currentState.currentValue + changeValue
                ).coerceIn(
                currentState.minValue,
                currentState.maxValue
            )

        if (newCurrentValue != currentState.currentValue) {
            _uiState.value = currentState.copy(
                currentValue = newCurrentValue
                /* Establece un nuevo valor al aplicar uno.
                valueToApply = ""
                 */
            )
        }
    }

    // Hace que la app pueda recordar el valor sin afectar a la barra.
    fun updateIntervalChangeValue(
        intervalId: Int,
        value: String
    ) {
        val filteredValue = value.filter { character ->
            character.isDigit() || character == '-'
        }

        _uiState.value = _uiState.value.copy(
            intervals = _uiState.value.intervals.map { interval ->
                if (interval.id == intervalId) {
                    interval.copy(changeValue = filteredValue)
                } else {
                    interval
                }
            }
        )

        restartIntervalIfNeeded(intervalId)
    }

    // Actualiza el intervalo de segundos
    fun updateIntervalSeconds(
        intervalId: Int,
        value: String
    ) {
        val filteredValue = value.filter { character ->
            character.isDigit() || character == '.'
        }

        val validationError = validateIntervalSeconds(filteredValue)

        _uiState.value = _uiState.value.copy(
            intervals = _uiState.value.intervals.map { interval ->
                if (interval.id == intervalId) {
                    interval.copy(
                        intervalSeconds = filteredValue,
                        validationError = validationError
                    )
                } else {
                    interval
                }
            }
        )

        if (validationError != null) {
            intervalJobs[intervalId]?.cancel()
            intervalJobs.remove(intervalId)

            updateIntervalEnabledState(intervalId, false)
        } else {
            restartIntervalIfNeeded(intervalId)
        }
    }

    // Esta y la siguiente funcion sirven para desactivar y activar los aplicadores repetitivos.
    fun setIntervalEnabled(
        intervalId: Int,
        enabled: Boolean
    ) {
        if (!enabled) {
            intervalJobs[intervalId]?.cancel()
            intervalJobs.remove(intervalId)

            updateIntervalEnabledState(intervalId, false)
            return
        }

        val interval = _uiState.value.intervals
            .find { it.id == intervalId }
            ?: return

        // Valicación para el Switch
        val validationError = validateIntervalSeconds(
            interval.intervalSeconds
        )

        if (validationError != null) {
            return
        }

        val changeValue = interval.changeValue.toLongOrNull()
            ?: return

        val intervalSeconds = interval.intervalSeconds.toDoubleOrNull()
            ?: return

        if (
            intervalSeconds < MIN_INTERVAL_SECONDS ||
            intervalSeconds > MAX_INTERVAL_SECONDS
        ) {
            return
        }

        updateIntervalEnabledState(intervalId, true)
        startIntervalJob(
            intervalId = intervalId,
            changeValue = changeValue,
            intervalSeconds = intervalSeconds
        )
    }

    private fun updateIntervalEnabledState(
        intervalId: Int,
        enabled: Boolean
    ) {
        _uiState.value = _uiState.value.copy(
            intervals = _uiState.value.intervals.map { interval ->
                if (interval.id == intervalId) {
                    interval.copy(enabled = enabled)
                } else {
                    interval
                }
            }
        )
    }

    private fun startIntervalJob(
        intervalId: Int,
        changeValue: Long,
        intervalSeconds: Double
    ) {
        intervalJobs[intervalId]?.cancel()

        intervalJobs[intervalId] = viewModelScope.launch {
            val delayMilliseconds = (intervalSeconds * 1000).toLong()

            while (true) {
                delay(delayMilliseconds)
                applyChange(changeValue)
            }
        }
    }

    // Reinicio al cambiar una configruación.
    private fun restartIntervalIfNeeded(intervalId: Int) {
        val interval = _uiState.value.intervals
            .find { it.id == intervalId }
            ?: return

        if (!interval.enabled) {
            return
        }

        setIntervalEnabled(intervalId, false)
        setIntervalEnabled(intervalId, true)
    }

    override fun onCleared() {
        intervalJobs.values.forEach { job ->
            job.cancel()
        }

        intervalJobs.clear()

        super.onCleared()
    }

    // Desplegable de aplicar intervalos
    fun toggleApplyIntervalsExpanded() {
        _uiState.value = _uiState.value.copy(
            applyIntervalsExpanded = !_uiState.value.applyIntervalsExpanded
        )
    }

    // Validaciones para los parametros de Intervalos
    private fun validateIntervalSeconds(
        value: String
    ): IntervalValidationError? {
        if (value.isBlank()) {
            return IntervalValidationError.EMPTY
        }

        val seconds = value.toDoubleOrNull()
            ?: return IntervalValidationError.INVALID_FORMAT

        return when {
            seconds < MIN_INTERVAL_SECONDS ->
                IntervalValidationError.TOO_SMALL

            seconds > MAX_INTERVAL_SECONDS ->
                IntervalValidationError.TOO_LARGE

            else -> null
        }
    }
}