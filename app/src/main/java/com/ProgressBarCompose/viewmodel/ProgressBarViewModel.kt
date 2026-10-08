package com.ProgressBarCompose.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

// Imports para el menu de intervalos
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

import com.ProgressBarCompose.data.IntervalValidationError  // Importación para la validación de Intervalos
import com.ProgressBarCompose.data.ProgressBarSettings  //Sirve para abrir configuraciones
import com.ProgressBarCompose.data.ProgressBarUiState
import com.ProgressBarCompose.data.SettingsValidationError

class ProgressBarViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(ProgressBarUiState())

    val uiState: StateFlow<ProgressBarUiState> = _uiState.asStateFlow()

    // Rangos aceptados para la cadencia en los valores aplicados automaticamente.
    private companion object {
        const val MIN_INTERVAL_SECONDS = 0.01
        const val MAX_INTERVAL_SECONDS = 256.0

        // Constantes de colores por defecto
        //const val DEFAULT_PROGRESS_COLOR = 0xFF4CAF50
        //const val DEFAULT_BACKGROUND_COLOR = 0xFFE0E0E0
        //const val DEFAULT_BORDER_COLOR = 0xFF333333
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

            _uiState.value = _uiState.value.copy(
                animationDuration = calculateAnimationDuration()
            )
        } else {
            restartIntervalIfNeeded(intervalId)

            _uiState.value = _uiState.value.copy(
                animationDuration = calculateAnimationDuration()
            )
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

            _uiState.value = _uiState.value.copy(
                animationDuration = calculateAnimationDuration()
            )

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
        // La animación se adapta al intervalo mas rapido.
        _uiState.value = _uiState.value.copy(
            animationDuration = calculateAnimationDuration()
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

    // Funciones para abrir CONFIGURACIONES
    fun openSettings() {
        val currentState = _uiState.value

        val settings = ProgressBarSettings(
            title = currentState.title,
            showImage = currentState.showImage,
            showCurrentValue = currentState.showCurrentValue,
            showMaxValue = currentState.showMaxValue,
            progressColor = currentState.progressColor,
            backgroundColor = currentState.backgroundColor,
            borderColor = currentState.borderColor,
            maxValue = currentState.maxValue.toString(),
            minValue = currentState.minValue.toString(),
            currentValue = currentState.currentValue.toString(),
            multicolorEnabled = currentState.multicolorEnabled,
            animationsEnabled = currentState.animationsEnabled
        )

        _uiState.value = currentState.copy(
            settingsDialogVisible = true,
            settingsDraft = settings
        )
    }

    // Funcion para restablecer configuraciones
    fun resetSettingsDraft() {
        _uiState.value = _uiState.value.copy(
            settingsDraft = ProgressBarSettings()
        )
    }

    // Cierra CONFIGURACIONES
    fun closeSettings() {
        _uiState.value = _uiState.value.copy(
            settingsDialogVisible = false
        )
    }

    // CONFIGURACIONES - TITULO: Cambia el nombre de la barra.
    fun updateSettingsTitle(value: String) {
        _uiState.value = _uiState.value.copy(
            settingsDraft = _uiState.value.settingsDraft.copy(
                title = value
            )
        )
    }

    // CONFIGURACIONES - MOSTRAR IMAGEN: Cambia la visibilidad de la imagen mostrada en la barra.
    fun updateSettingsShowImage(value: Boolean) {
        _uiState.value = _uiState.value.copy(
            settingsDraft = _uiState.value.settingsDraft.copy(
                showImage = value
            )
        )
    }

    // CONFIGURACIONES - MOSTRAR VALOR ACTUAL: Al activarse, la barra mostrara el valor actual que tenga.
    fun updateSettingsShowCurrentValue(value: Boolean) {
        _uiState.value = _uiState.value.copy(
            settingsDraft = _uiState.value.settingsDraft.copy(
                showCurrentValue = value,
                showMaxValue = if (value) {
                    _uiState.value.settingsDraft.showMaxValue
                } else {
                    false
                }
            )
        )
    }

    // CONFIGURACIONES - MOSTRAR VALOR MAXIMO: Al activarse MOSTRAR VALOR ACTUAL y esta, adicionalmente se mostrara el valor maximo que puede adquirir la barra.
    fun updateSettingsShowMaxValue(value: Boolean) {
        if (!_uiState.value.settingsDraft.showCurrentValue) {
            return
        }

        _uiState.value = _uiState.value.copy(
            settingsDraft = _uiState.value.settingsDraft.copy(
                showMaxValue = value
            )
        )
    }

    fun updateSettingsMaxValue(value: String) {
        val filteredValue = value.filter { it.isDigit() }

        _uiState.value = _uiState.value.copy(
            settingsDraft = _uiState.value.settingsDraft.copy(
                maxValue = filteredValue
            ),
            settingsValidationError = null
        )
    }

    fun updateSettingsMinValue(value: String) {
        val filteredValue = value.filter { it.isDigit() || it == '-' }

        _uiState.value = _uiState.value.copy(
            settingsDraft = _uiState.value.settingsDraft.copy(
                minValue = filteredValue
            ),
            settingsValidationError = null
        )
    }

    fun updateSettingsCurrentValue(value: String) {
        val filteredValue = value.filter { it.isDigit() || it == '-' }

        _uiState.value = _uiState.value.copy(
            settingsDraft = _uiState.value.settingsDraft.copy(
                currentValue = filteredValue
            ),
            settingsValidationError = null
        )
    }

    // CONFIGURACIONES - APLICAR CONFIGURACIONES: Aplica las configuraciones anteriormente establecidas.
    fun applySettings() {
        val draft = _uiState.value.settingsDraft

        val minValue = draft.minValue.toLongOrNull()
        val maxValue = draft.maxValue.toLongOrNull()
        val currentValue = draft.currentValue.toLongOrNull()

        val minLimit = -9_999_999L
        val maxLimit = 9_999_999L

        var minError: String? = null
        var maxError: String? = null
        var currentError: String? = null

        if (minValue == null) {
            minError = "Ingresa un valor mínimo válido."
        } else if (minValue < minLimit) {
            minError = "El mínimo permitido es -9,999,999."
        }

        if (maxValue == null) {
            maxError = "Ingresa un valor máximo válido."
        } else if (maxValue > maxLimit) {
            maxError = "El máximo permitido es 9,999,999."
        }

        if (currentValue == null) {
            currentError = "Ingresa un valor actual válido."
        }

        if (
            minValue != null &&
            maxValue != null &&
            minValue >= maxValue
        ) {
            minError = "Debe ser menor que el valor máximo."
            maxError = "Debe ser mayor que el valor mínimo."
        }

        if (
            currentValue != null &&
            minValue != null &&
            currentValue < minValue
        ) {
            currentError = "Debe estar dentro del rango permitido."
        }

        if (
            currentValue != null &&
            maxValue != null &&
            currentValue > maxValue
        ) {
            currentError = "Debe estar dentro del rango permitido."
        }

        val validationError = SettingsValidationError(
            minValueError = minError,
            maxValueError = maxError,
            currentValueError = currentError
        )

        if (
            minError != null ||
            maxError != null ||
            currentError != null
        ) {
            _uiState.value = _uiState.value.copy(
                settingsValidationError = validationError
            )
            return
        }

        _uiState.value = _uiState.value.copy(
            title = draft.title,
            minValue = minValue!!,
            maxValue = maxValue!!,
            currentValue = currentValue!!,
            showImage = draft.showImage,
            showCurrentValue = draft.showCurrentValue,
            showMaxValue = draft.showMaxValue,
            progressColor = draft.progressColor,
            backgroundColor = draft.backgroundColor,
            borderColor = draft.borderColor,
            multicolorEnabled = draft.multicolorEnabled,
            animationsEnabled = draft.animationsEnabled,
            settingsDialogVisible = false,
            settingsValidationError = null
        )
    }

    // Funciones para la parte de CONFIGURACION de Color Picker
    fun updateSettingsProgressColor(value: Long) {
        _uiState.value = _uiState.value.copy(
            settingsDraft = _uiState.value.settingsDraft.copy(
                progressColor = value
            )
        )
    }

    fun updateSettingsBackgroundColor(value: Long) {
        _uiState.value = _uiState.value.copy(
            settingsDraft = _uiState.value.settingsDraft.copy(
                backgroundColor = value
            )
        )
    }

    fun updateSettingsBorderColor(value: Long) {
        _uiState.value = _uiState.value.copy(
            settingsDraft = _uiState.value.settingsDraft.copy(
                borderColor = value
            )
        )
    }

    fun updateSettingsMulticolorEnabled(value: Boolean) {
        _uiState.value = _uiState.value.copy(
            settingsDraft = _uiState.value.settingsDraft.copy(
                multicolorEnabled = value
            )
        )
    }

    fun updateSettingsAnimationsEnabled(value: Boolean) {
        _uiState.value = _uiState.value.copy(
            settingsDraft = _uiState.value.settingsDraft.copy(
                animationsEnabled = value
            )
        )
    }

    // Se encarga de encontrar el intervalo mas rapido activado, sirve para evitar errores con la animación de barra.
    private fun getFastestIntervalSeconds(): Double? {
        return _uiState.value.intervals
            .filter { it.enabled }
            .minOfOrNull { it.intervalSeconds.toDoubleOrNull() ?: Double.MAX_VALUE }
    }

    // Cambia los MS de la animación de barra dependiendo del intervalor activo mas rapido.
    private fun calculateAnimationDuration(): Int {
        val fastestInterval = getFastestIntervalSeconds()
            ?: return 500

        return when {
            fastestInterval >= 1.0 -> 500
            fastestInterval >= 0.5 -> 250
            fastestInterval >= 0.1 -> 100
            fastestInterval >= 0.05 -> 50
            else -> 0 // No tiene sentido animar algo que ya va a velocidades impresionantes.
        }
    }
}