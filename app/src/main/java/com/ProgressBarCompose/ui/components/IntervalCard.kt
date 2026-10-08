package com.ProgressBarCompose.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment

import androidx.compose.ui.res.stringResource
import com.ProgressBarCompose.R
import com.ProgressBarCompose.data.IntervalValidationError

@Composable
fun IntervalCard(
    intervalId: Int,
    changeValue: String,
    intervalSeconds: String,
    enabled: Boolean,
    intervalError: IntervalValidationError?, // Validación de errores relacionados a INTERVALOS
    onChangeValue: (String) -> Unit,
    onIntervalSecondsChange: (String) -> Unit,
    onEnabledChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (enabled) {
                MaterialTheme.colorScheme.primaryContainer
            } else {
                MaterialTheme.colorScheme.surface
            }
        )
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Intervalo $intervalId"
                )

                Switch(
                    checked = enabled,
                    onCheckedChange = onEnabledChange,
                    enabled = intervalError == null
                )
            }

            OutlinedTextField(
                value = changeValue,
                onValueChange = onChangeValue,
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text("Cambio")
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                )
            )

            val errorMessage = when (intervalError) {
                IntervalValidationError.EMPTY ->
                    stringResource(R.string.interval_error_empty)

                IntervalValidationError.INVALID_FORMAT ->
                    stringResource(R.string.interval_error_invalid)

                IntervalValidationError.TOO_SMALL ->
                    stringResource(R.string.interval_error_too_small)

                IntervalValidationError.TOO_LARGE ->
                    stringResource(R.string.interval_error_too_large)

                null -> null
            }

            OutlinedTextField(
                value = intervalSeconds,
                onValueChange = onIntervalSecondsChange,
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text("Intervalo (segundos)")
                },
                singleLine = true,
                isError = intervalError != null,
                supportingText = {
                    errorMessage?.let { message ->
                        Text(message)
                    }
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Decimal
                )
            )
        }
    }
}