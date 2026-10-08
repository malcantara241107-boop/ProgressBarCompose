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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Surface
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.material3.HorizontalDivider
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.OutlinedButton


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

    // Multicolor y Animación
    onMulticolorEnabledChange: (Boolean) -> Unit,
    onAnimationsEnabledChange: (Boolean) -> Unit,

    onApplyChanges: () -> Unit,
    // Reset
    onResetSettings: () -> Unit,
) {
    Dialog(
        onDismissRequest = onDismiss
    ) {
        /*
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 650.dp),
            /*
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
             */
        ) {
         */
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 650.dp),
            shape = RoundedCornerShape(6.dp)
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Configurar Barra",
                        modifier = Modifier.weight(1f),
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.SemiBold
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

                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.surfaceVariant
                ) {
                    Text(
                        text = "Visualización",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        textAlign = TextAlign.Center,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

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
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Mostrar Imagen",
                        modifier = Modifier.weight(1f),
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium
                    )

                    Switch(
                        checked = settings.showImage,
                        onCheckedChange = onShowImageChange
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Mostrar valor actual",
                        modifier = Modifier.weight(1f),
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium
                    )

                    Switch(
                        checked = settings.showCurrentValue,
                        onCheckedChange = onShowCurrentValueChange
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Mostrar valor máximo",
                        modifier = Modifier.weight(1f),
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium
                    )

                    Switch(
                        checked = settings.showMaxValue,
                        onCheckedChange = onShowMaxValueChange,
                        enabled = settings.showCurrentValue
                    )
                }

                // Barra divisora
                HorizontalDivider(
                    thickness = 1.dp,
                    color = Color.LightGray
                )

                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.surfaceVariant
                ) {
                    Text(
                        text = "Valores",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        textAlign = TextAlign.Center,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

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

                // Barra divisora
                HorizontalDivider(
                    thickness = 1.dp,
                    color = Color.LightGray
                )

                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.surfaceVariant
                ) {
                    Text(
                        text = "Efectos",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        textAlign = TextAlign.Center,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                // Spacer(modifier = Modifier.height(4.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Fases de colores",
                        modifier = Modifier.weight(1f),
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium
                    )

                    Switch(
                        checked = settings.multicolorEnabled,
                        onCheckedChange = onMulticolorEnabledChange
                    )
                }

                // Spacer(modifier = Modifier.height(4.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Animación de progreso",
                        modifier = Modifier.weight(1f),
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium
                    )

                    Switch(
                        checked = settings.animationsEnabled,
                        onCheckedChange = onAnimationsEnabledChange
                    )
                }

                // Spacer(modifier = Modifier.height(16.dp))

                // Barra divisora
                HorizontalDivider(
                    thickness = 1.dp,
                    color = Color.LightGray
                )

                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.surfaceVariant
                ) {
                    Text(
                        text = stringResource(R.string.colors_section),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        textAlign = TextAlign.Center,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                /*
                Text(
                    text = stringResource(R.string.colors_section),
                    style = MaterialTheme.typography.titleMedium,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.SemiBold
                )
                */

                Text(
                    text = stringResource(R.string.progress_color),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium
                )

                ColorPicker(
                    selectedColor = settings.progressColor,
                    onColorSelected = onProgressColorChange
                )

                Text(
                    text = stringResource(R.string.background_color),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium
                )

                ColorPicker(
                    selectedColor = settings.backgroundColor,
                    onColorSelected = onBackgroundColorChange
                )

                Text(
                    text = stringResource(R.string.border_color),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium
                )

                ColorPicker(
                    selectedColor = settings.borderColor,
                    onColorSelected = onBorderColorChange
                )

                /*
                Text(
                    text = stringResource(R.string.colors_section),
                    style = MaterialTheme.typography.titleMedium
                )
                */

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = onApplyChanges,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Aplicar Cambios")
                }

                OutlinedButton(
                    onClick = onResetSettings,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Restablecer configuración")
                }
            }
        }
    }
}