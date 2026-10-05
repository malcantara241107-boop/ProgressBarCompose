package com.ProgressBarCompose.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.ProgressBarCompose.data.ProgressBarSettings
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
// Color Picker
import androidx.compose.ui.res.stringResource
import com.ProgressBarCompose.R
import com.ProgressBarCompose.ui.components.ColorPicker
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height


@Composable
fun ProgressBarSettingsDialog(
    settings: ProgressBarSettings,
    onDismiss: () -> Unit,
    onTitleChange: (String) -> Unit,
    onShowImageChange: (Boolean) -> Unit,
    onShowCurrentValueChange: (Boolean) -> Unit,
    onShowMaxValueChange: (Boolean) -> Unit,
    maxValueError: String?,
    minValueError: String?,
    currentValueError: String?,
    onMaxValueChange: (String) -> Unit,
    onMinValueChange: (String) -> Unit,
    onCurrentValueChange: (String) -> Unit,
    // Color Picker
    onProgressColorChange: (Long) -> Unit,
    onBackgroundColorChange: (Long) -> Unit,
    onBorderColorChange: (Long) -> Unit,

    onApplyChanges: () -> Unit,
    // Reset
    onResetSettings: () -> Unit,
) {
    Dialog(
        onDismissRequest = onDismiss
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 650.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Configurar Barra",
                        modifier = Modifier.weight(1f),
                        style = MaterialTheme.typography.headlineSmall
                    )

                    IconButton(
                        onClick = onDismiss
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Cerrar configuración"
                        )
                    }
                }

                Text(
                    text = "Personalización",
                    style = MaterialTheme.typography.titleMedium
                )

                OutlinedTextField(
                    value = settings.title,
                    onValueChange = onTitleChange,
                    modifier = Modifier.fillMaxWidth(),
                    label = {
                        Text("Nombre de Barra")
                    },
                    singleLine = true
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Mostrar Imagen",
                        modifier = Modifier.weight(1f)
                    )

                    Switch(
                        checked = settings.showImage,
                        onCheckedChange = onShowImageChange
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Mostrar valor actual",
                        modifier = Modifier.weight(1f)
                    )

                    Switch(
                        checked = settings.showCurrentValue,
                        onCheckedChange = onShowCurrentValueChange
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Mostrar valor máximo actual",
                        modifier = Modifier.weight(1f)
                    )

                    Switch(
                        checked = settings.showMaxValue,
                        onCheckedChange = onShowMaxValueChange,
                        enabled = settings.showCurrentValue
                    )
                }

                Text(
                    text = "Valores",
                    style = MaterialTheme.typography.titleMedium
                )

                OutlinedTextField(
                    value = settings.maxValue,
                    onValueChange = onMaxValueChange,
                    modifier = Modifier.fillMaxWidth(),
                    label = {
                        Text("Valor máximo")
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    ),
                    isError = maxValueError != null,
                    supportingText = {
                        maxValueError?.let {
                            Text(it)
                        }
                    }
                )

                OutlinedTextField(
                    value = settings.minValue,
                    onValueChange = onMinValueChange,
                    modifier = Modifier.fillMaxWidth(),
                    label = {
                        Text("Valor mínimo")
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    ),
                    isError = minValueError != null,
                    supportingText = {
                        minValueError?.let {
                            Text(it)
                        }
                    }
                )

                OutlinedTextField(
                    value = settings.currentValue,
                    onValueChange = onCurrentValueChange,
                    modifier = Modifier.fillMaxWidth(),
                    label = {
                        Text("Valor actual")
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    ),
                    isError = currentValueError != null,
                    supportingText = {
                        currentValueError?.let {
                            Text(it)
                        }
                    }
                )

                Text(
                    text = stringResource(R.string.colors_section),
                    style = MaterialTheme.typography.titleMedium
                )

                Text(
                    text = stringResource(R.string.progress_color)
                )

                ColorPicker(
                    selectedColor = settings.progressColor,
                    onColorSelected = onProgressColorChange
                )

                Text(
                    text = stringResource(R.string.background_color)
                )

                ColorPicker(
                    selectedColor = settings.backgroundColor,
                    onColorSelected = onBackgroundColorChange
                )

                Text(
                    text = stringResource(R.string.border_color)
                )

                ColorPicker(
                    selectedColor = settings.borderColor,
                    onColorSelected = onBorderColorChange
                )

                Text(
                    text = stringResource(R.string.colors_section),
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = onApplyChanges,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Aplicar Cambios")
                }

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = onResetSettings,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Restablecer configuración")
                }
            }
        }
    }
}